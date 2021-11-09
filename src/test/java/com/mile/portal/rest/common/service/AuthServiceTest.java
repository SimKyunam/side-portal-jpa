package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.domain.Client;
import com.mile.portal.rest.user.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

class AuthServiceTest {
    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private ManagerRepository managerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.passwordEncoder = new BCryptPasswordEncoder();

        authService = new AuthService(
                authenticationManagerBuilder,
                passwordEncoder,
                jwtTokenProvider,
                refreshTokenRepository,
                clientRepository,
                managerRepository);
    }

    @DisplayName("1. 사용자 등록")
    @Test
    void test_1(){

    }

    public ReqLogin createReqLogin() {

        return ReqLogin.builder()
                .loginId("test")
                .loginPwd(passwordEncoder.encode("1111"))
                .userName("홍길동")
                .userType(Authority.ROLE_USER)
                .icisNo("A12345678")
                .status("ACT")
                .build();
    }
}