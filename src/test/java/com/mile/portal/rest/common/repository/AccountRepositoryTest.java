package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.user.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("1. 사용자 중복 체크")
    @Test
    void test_1(){
        //given
        String loginId = "test";

        //when
        boolean existsByLoginId = userRepository.existsByLoginId(loginId);

        //then
        assertEquals(existsByLoginId, false);
    }

}