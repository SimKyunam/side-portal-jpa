package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.service.AuthService;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.user.model.domain.Client;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setUp(){
        passwordEncoder = new BCryptPasswordEncoder();

        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("1. 사용자 생성")
    @Test
    void test_1() throws Exception {
        Client client = createClient();
        given(authService.createUser(any())).willReturn(client);

        mvc.perform(get("/v1/api/common/createUser")
                .content("{\n" +
                        "    \"loginId\": \"test\",\n" +
                        "    \"loginPwd\" : \"1111\",\n" +
                        "    \"userName\" : \"홍길동\",\n" +
                        "    \"userType\" : \"ROLE_USER\",\n" +
                        "    \"icisNo\": \"A12345678\",\n" +
                        "    \"activeYn\" : \"Y\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test")));
    }
    
    @DisplayName("2. 사용자 로그인")
    @Test
    void test_2(){
    
    }
    
    @DisplayName("3. 관리자 생성")
    @Test
    void test_3(){
    
    }
    
    @DisplayName("4. 관리자 로그인")
    @Test
    void test_4(){
    
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

    //관리자 생성
    public Manager createManager() {
        Manager manager = Manager.builder()
                .loginId("admin")
                .loginPwd(passwordEncoder.encode("1111"))
                .name("관리자")
                .type(Authority.ROLE_ADMIN)
                .status("ACT")
                .build();

        return manager;
    }
}