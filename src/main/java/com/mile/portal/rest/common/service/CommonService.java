package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.domain.RefreshToken;
import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.domain.User;
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
    public User createUser(ReqCommon.UserLogin userLogin) {
        User user = User.builder()
                .userName(userLogin.getUserName())
                .userType(userLogin.getUserType())
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .icisNo(userLogin.getIcisNo())
                .activeYn(userLogin.getActiveYn())
                .build();

        return userRepository.save(user);
    }

    public ReqToken loginUser(ReqCommon.UserLogin userLogin) {
        LoginUser user = LoginUser.builder()
                .loginId(userLogin.getLoginId())
                .password(userLogin.getLoginPwd())
                .build();

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication();

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
