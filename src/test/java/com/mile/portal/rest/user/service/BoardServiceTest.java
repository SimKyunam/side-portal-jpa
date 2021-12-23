package com.mile.portal.rest.user.service;

import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.user.repository.BoardNoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @MockBean
    BoardNoticeRepository boardNoticeRepository;

    @DisplayName("1. 공지사항 생성")
    @Test
    void test_1() {
        //given
        ReqBoard.BoardNotice reqNotice = createReqNotice();
        BoardNotice notice = createNotice();
        given(boardNoticeRepository.save(any())).willReturn(notice);

        //when
        BoardNotice boardNotice = boardService.createBoardNotice(reqNotice);

        //then
        assertEquals(boardNotice.getTitle(), reqNotice.getTitle());
        verify(boardNoticeRepository, times(1)).save(any());
    }

    private ReqBoard.BoardNotice createReqNotice() {
        return ReqBoard.BoardNotice.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .ntcType("NOT")
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .hotYn("N")
                .pubYn("Y")
                .build();
    }

    private BoardNotice createNotice() {
        return BoardNotice.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .ntcType("NOT")
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .hotYn("N")
                .pubYn("Y")
                .build();
    }

}