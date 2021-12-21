package com.mile.portal.rest.common.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CodeRepositoryTest {

    @Autowired
    CodeRepository codeRepository;

    @Test
    @DisplayName("1. 목록")
    void test1() {
        System.out.println(codeRepository.findTreeAll());
    }

    @Test
    @DisplayName("2. 목록")
    void test2() {
        System.out.println(codeRepository.findTreeAll());
    }
}