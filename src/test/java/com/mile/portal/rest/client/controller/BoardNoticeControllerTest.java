package com.mile.portal.rest.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.portal.rest.client.service.BoardNoticeService;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser
class BoardNoticeControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardNoticeService boardNoticeService;

    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("1. 공지사항 목록 조회")
    void test1() throws Exception {
        Page<BoardNoticeDto> noticePaging = createNoticePaging();

        //given
        given(boardNoticeService.listBoardNotice(any(), any())).willReturn(noticePaging);

        //when
        mvc.perform(get("/api/v1/board/notice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].title").value("테스트 타이틀"));

        //then
        then(boardNoticeService).should().listBoardNotice(any(), any());
    }

    @Test
    @DisplayName("2. 공지사항 상세")
    void test2() throws Exception {
        //given
        given(boardNoticeService.selectBoardNotice(any())).willReturn(createNoticeDto());

        //when
        mvc.perform(get("/api/v1/board/notice/1"))
                .andExpect(jsonPath("$.data.title").value("테스트 타이틀"))
                .andExpect(status().isOk());

        //then
        then(boardNoticeService).should().selectBoardNotice(any());
    }

    BoardNotice createNotice() {
        return BoardNotice.builder()
                .title("테스트 타이틀").content("테스트 내용")
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .pubYn("Y").hotYn("N")
                .build();
    }

    BoardNoticeDto createNoticeDto() {
        return BoardNoticeDto.builder()
                .title("테스트 타이틀").content("테스트 내용")
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .pubYn("Y").hotYn("N")
                .build();
    }

    Page<BoardNoticeDto> createNoticePaging() {
        List<BoardNoticeDto> boardNoticeList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        IntStream.range(0, 5)
                .forEach(i -> boardNoticeList.add(createNoticeDto()));

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, boardNoticeList::size);
    }
}