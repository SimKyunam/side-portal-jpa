package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.domain.RefreshToken;
import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.domain.Client;
import com.mile.portal.rest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    @Transactional
    public Client createUser(ReqLogin userLogin) {
        Client user = Client.builder()
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .name(userLogin.getUserName())
                .type(userLogin.getUserType())
                .icisNo(userLogin.getIcisNo())
                .status(userLogin.getStatus())
                .build();

        return userRepository.save(user);
    }

    @Transactional
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

    @Transactional
    public Manager createMng(ReqLogin userLogin) {
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

    @Transactional
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

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

}
