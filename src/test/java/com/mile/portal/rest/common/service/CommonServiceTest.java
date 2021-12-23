package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
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

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        final String codeId = "actionCode";

        //given
        ReqCommon.Code reqCode = createReqCode(codeId, null);
        given(codeRepository.countByParentIsNullAndCodeNot(eq(codeId))).willReturn(3L);
        given(codeRepository.save(any())).willReturn(createCode(reqCode));

        //when
        Code code = commonService.createCode(reqCode);

        //then
        then(codeRepository).should(times(1)).countByParentIsNullAndCodeNot(eq(codeId));
        then(codeRepository).should(times(1)).save(any());

        assertEquals(code.getCode(), codeId);
    }

    @Test
    @DisplayName("2. 부모가 있는 경우")
    void test2() {
        final String codeId = "update";
        final String parentId = "actionCode";

        //given
        ReqCommon.Code reqCode = createReqCode(codeId, parentId);
        given(codeRepository.findParentCode(eq(parentId), eq(codeId))).willReturn(createCodeDto());
        given(codeRepository.save(any())).willReturn(createCode(reqCode));

        //when
        Code code = commonService.createCode(reqCode);

        //then
        then(codeRepository).should(times(1)).findParentCode(eq(parentId), eq(codeId));
        assertEquals(code.getCode(), codeId);
    }
    
    @Test
    @DisplayName("3. update-code가 존재하는 경우")
    void test3() {
        final String codeId = "actionCode";
        //given
        ReqCommon.Code reqCode = createReqCode(codeId, null);
        Optional<Code> code = Optional.of(createCode(reqCode));
        given(codeRepository.findById(codeId)).willReturn(code);
        given(codeRepository.save(any())).willReturn(createCode(reqCode));

        //when
        Code updatedCode = commonService.updateCode(reqCode);

        //then
        then(codeRepository).should().save(any());
        then(codeRepository).should().findById(any());
        assertEquals(updatedCode.getCode(), codeId);
    }

    @Test
    @DisplayName("4. update-code가 존재하지 않는 경우")
    void test4() {
        final String codeId = "actionCode";
        //given
        ReqCommon.Code reqCode = createReqCode(codeId, null);
        given(codeRepository.findById(codeId)).willReturn(Optional.empty());

        //when
        assertThrows(ResultNotFoundException.class, () -> {
            commonService.updateCode(reqCode);
        });

        //then
        then(codeRepository).should().findById(any());
    }
    
    CodeDto createCodeDto() {
        return CodeDto.builder()
                .code("actionCode")
                .codeName("액션코드")
                .codeValue("ActionCode")
                .ord(1)
                .depth(1)
                .childCount(3L)
                .build();
    }

    ReqCommon.Code createReqCode(String codeId, String parentId) {
        return ReqCommon.Code.builder()
                .codeId(codeId)
                .codeName("테스트코드")
                .codeValue(codeId.toUpperCase(Locale.ROOT))
                .parentId(parentId)
                .build();
    }

    Code createCode(ReqCommon.Code reqCode) {
        Code code = Code.builder()
                .code(reqCode.getCodeId())
                .codeName("테스트코드")
                .codeValue(reqCode.getCodeId().toUpperCase(Locale.ROOT))
                .depth(1)
                .ord(4)
                .build();

        if(reqCode.getParentId() != null && reqCode.getParentId().isEmpty()) {
            code.setParent(Code.builder().code(reqCode.getParentId()).build());
        }

        return code;
    }
}