package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private AuthService authService;
    
    @DisplayName("1. 사용자 생성")
    @Test
    void test_1(){
    
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
    
}