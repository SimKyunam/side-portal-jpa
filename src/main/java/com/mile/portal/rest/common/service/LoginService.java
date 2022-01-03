package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.client.model.domain.Client;
import com.mile.portal.rest.client.repository.ClientRepository;
import com.mile.portal.rest.common.model.comm.ReqToken;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.domain.RefreshToken;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;

    /**
     * 공통 처리
     */
    public ReqToken loginAuthenticate(Account user) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String loginId = authentication.getName();
        Account accountDomain;
        if (authorities.contains(Authority.ROLE_USER.getAuthority())) {
            accountDomain = clientRepository.findByLoginId(loginId).orElseThrow(RuntimeException::new);
        } else {
            accountDomain = managerRepository.findByLoginId(loginId).orElseThrow(RuntimeException::new);
        }

        ReqToken tokenDto = jwtTokenProvider.generateTokenDto(authentication, accountDomain);
        String format = DateTimeUtil.millisToDate(tokenDto.getAccessTokenExpiresIn(),
                "YYYY-MM-DD HH:mm:ss.SSS");

        if (authorities.contains(Authority.ROLE_USER.getAuthority())) {
            accountDomain.setTokenId(tokenDto.getAccessToken());
            accountDomain.setTokenExprDt(format);
            Client client = (Client) accountDomain;

            clientRepository.save(client);
        } else {
            accountDomain.setTokenId(tokenDto.getAccessToken());
            accountDomain.setTokenExprDt(format);
            Manager manager = (Manager) accountDomain;

            managerRepository.save(manager);
        }

        // 4. RefreshToken 저장
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseGet(RefreshToken::new);

        refreshToken.setKey(authentication.getName());
        refreshToken.setValue(tokenDto.getRefreshToken());

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }
}
