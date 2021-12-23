package com.mile.portal.rest.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.service.AuthService;
import com.mile.portal.rest.common.service.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CommonControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommonService commonService;

    @BeforeEach
    public void setUp(){
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("1. 코드 목록")
    void test1() throws Exception {
        //given
        given(commonService.listCode()).willReturn(createMenuList());

        //when
        mvc.perform(get("/api/v1/common/code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code").value("resourceCd"));

        //then
        then(commonService).should().listCode();
    }

    @Test
    @DisplayName("2. 코드 상세 조회")
    void test2() throws Exception {
        //given
        given(commonService.selectCode(any(), any())).willReturn(createResourceCd());

        //when
        mvc.perform(get("/api/v1/common/code/resourceCd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("resourceCd"));

        //then
        then(commonService).should().selectCode(any(), any());
    }

    @Test
    @DisplayName("3. 코드 생성")
    void test3() throws Exception {
        //given
        ReqCommon.Code reqCode = ReqCommon.Code.builder()
                .codeId("actionCode").codeName("액션코드").codeValue("ActionCode").build();
        String objStr = objectMapper.writeValueAsString(reqCode);

        //when
        mvc.perform(post("/api/v1/common/code/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objStr))
                .andExpect(status().isOk());

        //then
        then(commonService).should().createCode(any());
    }

    @Test
    @DisplayName("4. 코드 수정")
    void test4() throws Exception {
        //given
        ReqCommon.Code reqCode = ReqCommon.Code.builder()
                .codeId("resourceCd").codeName("제공자유형").codeValue("수정 Value").build();
        String objStr = objectMapper.writeValueAsString(reqCode);

        //when
        mvc.perform(put("/api/v1/common/code/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objStr))
                .andExpect(status().isOk());

        //then
        then(commonService).should().updateCode(any());
    }

    @Test
    @DisplayName("5. 코드 수정 - 필수값 없는 경우")
    void test5() throws Exception {
        //given
        ReqCommon.Code reqCode = ReqCommon.Code.builder()
                .codeId("").codeName("제공자유형").codeValue("수정 Value").build();
        String objStr = objectMapper.writeValueAsString(reqCode);

        //when
        mvc.perform(put("/api/v1/common/code/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objStr))
                .andExpect(status().isBadRequest());

        //then
        then(commonService).should(never()).updateCode(any());
    }

    @Test
    @DisplayName("6. 코드 삭제")
    void test6() throws Exception {
        String codeId = "resourceCd";

        //when
        mvc.perform(delete("/api/v1/common/code/" + codeId))
                .andExpect(status().isOk());

        //then
        then(commonService).should().deleteCode(eq(codeId));
    }

    Code createResourceCd() {
        Code resourceCode = Code.builder()
                .code("resourceCd").codeName("자원유형").codeValue("ResourceCd")
                .ord(1).depth(1)
                .build();
        Code resourceChildCode1 = Code.builder()
                .code("kt").codeName("KT").codeValue("1").ord(1).depth(2)
                .parent(resourceCode)
                .build();
        Code resourceChildCode2 = Code.builder()
                .code("aws").codeName("AWS").codeValue("2").ord(2).depth(2)
                .parent(resourceCode)
                .build();

        resourceCode.setChild(Arrays.asList(resourceChildCode1, resourceChildCode2));

        return resourceCode;
    }

    Code createNoticeType() {
        Code noticeTypeCode = Code.builder()
                .code("noticeType").codeName("공지사항구분").codeValue("NoticeType")
                .ord(2).depth(1)
                .build();
        Code noticeTypeChildCode1 = Code.builder()
                .code("SVC").codeName("서비스").codeValue("SVC").ord(1).depth(2)
                .parent(noticeTypeCode)
                .build();
        Code noticeTypeChildCode2 = Code.builder()
                .code("UPD").codeName("업데이트").codeValue("UPD").ord(2).depth(2)
                .parent(noticeTypeCode)
                .build();
        Code noticeTypeChildCode3 = Code.builder()
                .code("WRK").codeName("작업/장애").codeValue("WRK").ord(3).depth(2)
                .parent(noticeTypeCode)
                .build();

        noticeTypeCode.setChild(Arrays.asList(noticeTypeChildCode1, noticeTypeChildCode2));

        return noticeTypeCode;
    }

    List<Code> createMenuList() {
        return Arrays.asList(createResourceCd(), createNoticeType());
    }
}