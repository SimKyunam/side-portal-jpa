package com.mile.portal.rest.common.service;

import com.mile.portal.rest.common.repository.CodeRepository;
import com.mile.portal.rest.common.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private CodeRepository codeRepository;
    
    @Test
    @DisplayName("1. list 테스트")
    void test1() {
    }
    
    @Test
    @DisplayName("2. select 테스트")
    void test2() {
    }
    
    @Test
    @DisplayName("3. insert 테스트")
    void test3() {
    }
    
    @Test
    @DisplayName("4. update 테스트")
    void test4() {
    }
    
    @Test
    @DisplayName("5. delete 테스트")
    void test5() {
    }
}