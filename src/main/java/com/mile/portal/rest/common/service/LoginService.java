package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.comm.ReqToken;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.domain.RefreshToken;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    /**
     * 공통 처리
     */
    public ReqToken loginAuthenticate(Account user) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication();

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 계정 정보 체크
        String loginId = authentication.getName();
        Account accountDomain = userRepository.findByLoginIdAndEmailCheckYn(loginId, "Y")
                .orElseThrow(() -> new ResultNotFoundException("이메일 승인이 필요합니다."));

        // 계정 마지막 로그인, IP 수정
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        accountDomain.setLastLoginDt(LocalDateTime.now());
        accountDomain.setLastLoginIp(req.getRemoteAddr());
        userRepository.save(accountDomain);

        // 인증 정보를 기반으로 JWT 토큰 생성
        ReqToken tokenDto = jwtTokenProvider.generateTokenDto(authentication, accountDomain);

        // RefreshToken 저장
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseGet(RefreshToken::new);

        refreshToken.setKey(authentication.getName());
        refreshToken.setValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }
}
