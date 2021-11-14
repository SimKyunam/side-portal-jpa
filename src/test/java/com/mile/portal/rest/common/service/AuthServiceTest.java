package com.mile.portal.rest.common.service;

import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.RefreshTokenRepository;
import com.mile.portal.rest.common.repository.UserRepository;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.domain.Client;
import com.mile.portal.rest.user.repository.ClientRepository;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.PersistenceContext;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private LoginService loginService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ClientRepository clientRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @DisplayName("1. 사용자 등록")
    @Test
    void test_1(){
        //given
        ReqLogin reqLogin = createReqLogin();
        Client client = createClient();
        given(clientRepository.save(any())).willReturn(client);

        //when
        Client newUser = authService.createUser(reqLogin);

        //then
        verify(userRepository).existsByLoginId(eq(reqLogin.getLoginId()));
        verify(clientRepository).save(any());

        assertEquals(reqLogin.getLoginId(), newUser.getLoginId());
    }

    @DisplayName("2. 중복 사용자 등록")
    @Test
    void test_2(){
        //given
        ReqLogin reqLogin = createReqLogin();
        Client client = createClient();

        //when
        given(clientRepository.save(any())).willReturn(client);
        given(userRepository.existsByLoginId(eq(reqLogin.getLoginId()))).willReturn(true);

        //then
        assertThrows(RuntimeException.class, () -> {
            authService.createUser(reqLogin);
        });
    }

    @DisplayName("3. 로그인 재시도")
    @Test
    @WithMockUser
    void test_3(){
        //given
        ReqCommon.UserLogin userLogin = createUserLogin();
        given(loginService.loginAuthenticate(any())).willReturn(new ReqToken());

        //when
        when(authService.loginUser(userLogin)).thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class, ()-> {
            authService.loginUser(userLogin);
        });

        //then
        verify(loginService, times(3)).loginAuthenticate(any());
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

    //사용자 생성
    public Client createClient() {
        Client build = Client.builder()
                .loginId("test")
                .loginPwd(passwordEncoder.encode("1111"))
                .name("홍길동")
                .type(Authority.ROLE_USER)
                .icisNo("A12345678")
                .status("ACT")
                .build();

        return build;
    }

    //사용자 로그인
    public ReqCommon.UserLogin createUserLogin() {
        return ReqCommon.UserLogin.builder()
                .loginId("test")
                .loginPwd(passwordEncoder.encode("1111"))
                .build();
    }

    //로그인
    public LoginUser createLoginUser(){
        return LoginUser.builder()
                .loginId("test")
                .password(passwordEncoder.encode("1111"))
                .permission(Authority.ROLE_ADMIN)
                .build();
    }

    public UsernamePasswordAuthenticationToken createAuthentication(){
        LoginUser user = this.createLoginUser();
        return user.toAuthentication();
    }
}