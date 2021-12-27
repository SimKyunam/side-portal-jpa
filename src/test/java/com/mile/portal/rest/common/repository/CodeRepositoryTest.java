package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.model.dto.CodeNativeDto;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @DisplayName("2. 코드 상세 조회")
    void test2() {
        System.out.println(codeRepository.findTreeCode("resourceCd", ""));
    }

    @Test
    @DisplayName("3. 부모 코드")
    void test3() {
        CodeDto code = codeRepository.findParentCode("resourceCd", "");
        assertEquals(code.getChildCount(), 3);
        System.out.println(code);
    }

    @Test
    @DisplayName("4. 카운팅")
    void test4() {
        long count = codeRepository.count();
        System.out.println(count);
        
        long parentCount = codeRepository.countByParentIsNullAndCodeNot("resourceCd");
        System.out.println(parentCount);
    }

    @Test
    @DisplayName("5. native query")
    void test5() {
        List<CodeNativeDto> resourceCd = codeRepository.findByCodeChild("resourceCd");
        System.out.println(resourceCd);
    }

    @Test
    @DisplayName("6. native query -> queryDsl ")
    void test6() {
        List<CodeDto> codeDtoList = codeRepository.findByCodeChildren("resourceCd", "noticeType");
        System.out.println(codeDtoList);
    }
}