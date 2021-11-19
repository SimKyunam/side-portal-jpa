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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    
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

    @Retryable(
        value = {BadCredentialsException.class},
        backoff = @Backoff(delay = 1000)
    )
    public ReqToken loginUser(ReqCommon.UserLogin userLogin) {
        LoginUser user = LoginUser.builder()
                .loginId(userLogin.getLoginId())
                .password(userLogin.getLoginPwd())
                .permission(Authority.ROLE_USER)
                .build();

        // 로그인 처리
        return loginService.loginAuthenticate(user);
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

    @Retryable(
            value = {BadCredentialsException.class},
            backoff = @Backoff(delay = 1000)
    )
    public ReqToken loginMng(ReqCommon.UserLogin userLogin) {
        LoginUser user = LoginUser.builder()
                .loginId(userLogin.getLoginId())
                .password(userLogin.getLoginPwd())
                .permission(Authority.ROLE_ADMIN)
                .build();

        // 로그인 처리
        return loginService.loginAuthenticate(user);
    }


    


}