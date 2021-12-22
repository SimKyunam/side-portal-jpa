package com.mile.portal.rest.common.service;

import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.repository.CodeRepository;
import com.mile.portal.rest.common.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CommonServiceTest {
    @Autowired
    private CommonService commonService;

    @MockBean
    private CodeRepository codeRepository;
    
    @Test
    @DisplayName("1. insert 부모가 없는 경우")
    void test1() {
        final String parentCode = "actionCode";

        //given
        given(codeRepository.countByParentIsNullAndCodeNot(eq(parentCode))).willReturn(3L);
        given(codeRepository.save(any())).willReturn(createCode());

        //when
        Code code = commonService.createCode(createReqCode());

        //then
        verify(codeRepository, times(1)).countByParentIsNullAndCodeNot(eq(parentCode));
        verify(codeRepository, times(1)).save(any());

        assertEquals(code.getCode(), parentCode);
    }

    @Test
    @DisplayName("2. 부모가 있는 경우")
    void test2() {
        final String parentCode = "actionCode";
        final String childCode = "update";

        //given
        given(codeRepository.findParentCode(eq(parentCode), eq(childCode)))
                .willReturn(createCodeDto());

        //when
        Code code = commonService.createCode(createReqCode());

        //then
        verify(codeRepository, times(1)).findParentCode(eq(parentCode), eq(childCode));
        assertEquals(code.getCode(), childCode);
    }
    
    @Test
    @DisplayName("3. update 테스트")
    void test3() {
    }
    
    CodeDto createCodeDto() {
        return CodeDto.builder()
                .code("actionCode")
                .codeName("액션코드")
                .codeValue("ActionCode")
                .ord(1)
                .depth(1)
                .build();
    }

    ReqCommon.Code createReqCode() {
        return ReqCommon.Code.builder()
                .codeId("actionCode")
                .codeName("액션코드")
                .codeValue("ActionCode")
                .build();
    }

    Code createCode() {
        return Code.builder()
                .code("actionCode")
                .codeName("액션코드")
                .codeValue("ActionCode")
                .depth(1)
                .ord(4)
                .build();
    }
}