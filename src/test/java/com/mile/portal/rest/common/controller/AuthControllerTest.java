package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.comm.ReqToken;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.service.AuthService;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.user.model.domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    PasswordEncoder passwordEncoder;

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
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @DisplayName("1. 사용자 생성")
    @Test
    void test_1() throws Exception {
        Client client = createClient();
        given(authService.createUser(any())).willReturn(client);

        mvc.perform(post("/api/v1/common/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"loginId\": \"test\",\n" +
                        "    \"loginPwd\" : \"1111\",\n" +
                        "    \"userName\" : \"홍길동\",\n" +
                        "    \"userType\" : \"ROLE_USER\",\n" +
                        "    \"icisNo\": \"A12345678\",\n" +
                        "    \"activeYn\" : \"Y\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }
    
    @DisplayName("2. 사용자 로그인")
    @Test
    void test_2() throws Exception {
        given(authService.loginUser(any())).willReturn(createToken());

        mvc.perform(post("/api/v1/common/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"loginId\": \"test\",\n" +
                        "    \"loginPwd\" : \"1111\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }
    
    @DisplayName("3. 관리자 생성")
    @Test
    void test_3() throws Exception {
        Manager manager = createManager();
        given(authService.createMng(any())).willReturn(manager);

        mvc.perform(post("/api/v1/common/createMng")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"loginId\": \"testAdmin\",\n" +
                        "    \"loginPwd\" : \"1111\",\n" +
                        "    \"userName\" : \"관리자\",\n" +
                        "    \"userType\" : \"ROLE_ADMIN\",\n" +
                        "    \"activeYn\" : \"Y\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }
    
    @DisplayName("4. 관리자 로그인")
    @Test
    void test_4() throws Exception {
        given(authService.loginMng(any())).willReturn(createToken());

        mvc.perform(post("/api/v1/common/loginMng")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"loginId\": \"testAdmin\",\n" +
                        "    \"loginPwd\" : \"1111\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }

    //사용자 생성
    public Client createClient() {
        Client build = Client.builder()
                .loginId("test")
                .loginPwd(passwordEncoder.encode("1111"))
                .name("홍길동")
                .permission(Authority.ROLE_USER)
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
                .permission(Authority.ROLE_ADMIN)
                .status("ACT")
                .build();

        return manager;
    }

    public ReqToken createToken() {
        ReqToken reqToken = ReqToken.builder()
                .grantType("grant")
                .accessToken("accessToekn")
                .refreshToken("refreshToekn")
                .accessTokenExpiresIn(123123123123123123L)
                .build();

        return reqToken;
    }
}