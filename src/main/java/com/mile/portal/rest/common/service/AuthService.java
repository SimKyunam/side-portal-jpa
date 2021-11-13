package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.domain.RefreshToken;
import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.common.repository.UserRepository;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.domain.Client;
import com.mile.portal.rest.user.repository.ClientRepository;
import com.mile.portal.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;

    public Client createUser(ReqLogin userLogin) {
        if(userRepository.existsByLoginId(userLogin.getLoginId())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Client user = Client.builder()
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .name(userLogin.getUserName())
                .type(userLogin.getUserType())
                .icisNo(userLogin.getIcisNo())
                .status(userLogin.getStatus())
                .build();

        return clientRepository.save(user);
    }

    public ReqToken loginUser(ReqCommon.UserLogin userLogin) {
        LoginUser user = LoginUser.builder()
                .loginId(userLogin.getLoginId())
                .password(userLogin.getLoginPwd())
                .permission(Authority.ROLE_USER)
                .build();

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication();

        // 2-4 단게 처리
        return loginAuthenticate(authenticationToken);
    }

    public Manager createMng(ReqLogin userLogin) {
        if(userRepository.existsByLoginId(userLogin.getLoginId())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Manager manager = Manager.builder()
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .name(userLogin.getUserName())
                .type(userLogin.getUserType())
                .email(userLogin.getEmail())
                .phone(userLogin.getPhone())
                .status(userLogin.getStatus())
                .build();

        return managerRepository.save(manager);
    }

    public ReqToken loginMng(ReqCommon.UserLogin userLogin) {
        LoginUser user = LoginUser.builder()
                .loginId(userLogin.getLoginId())
                .password(userLogin.getLoginPwd())
                .permission(Authority.ROLE_ADMIN)
                .build();

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication();

        // 2-4 단게 처리
        return loginAuthenticate(authenticationToken);
    }


    /** 공통 처리 */
    public ReqToken loginAuthenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        ReqToken tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String loginId = jwtTokenProvider.getTokenId(tokenDto.getAccessToken());
        String format = DateTimeUtil.millisToDate(tokenDto.getAccessTokenExpiresIn(),
                "YYYY-MM-DD HH:mm:ss.SSS");

        if(authorities.contains(Authority.ROLE_USER.getAuthority())){
            Client client = clientRepository.findByLoginId(loginId).orElseThrow(RuntimeException::new);

            client.setTokenId(tokenDto.getAccessToken());
            client.setTokenExprDt(format);
            clientRepository.save(client);
        }else{
            Manager manager = managerRepository.findByLoginId(loginId).orElseThrow(RuntimeException::new);

            manager.setTokenId(tokenDto.getAccessToken());
            manager.setTokenExprDt(format);
            managerRepository.save(manager);
        }

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }


}
