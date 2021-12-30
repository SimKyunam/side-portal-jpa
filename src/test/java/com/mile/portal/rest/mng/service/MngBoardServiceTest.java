package com.mile.portal.rest.mng.service;

import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
class MngBoardServiceTest {
    @Autowired
    MngBoardService mngBoardService;

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
        BoardNotice boardNotice = mngBoardService.createBoardNotice(reqNotice, null, 1L);

        //then
        assertEquals(boardNotice.getTitle(), reqNotice.getTitle());
        then(boardNoticeRepository).should().save(any());
    }

    @Test
    @DisplayName("2. 공지사항 삭제")
    void test2() {
        IntStream.range(0, 5).forEach(i -> mngBoardService.createBoardNotice(createReqNotice(), null, 1L));

        String ids = "1, 2, ,,";
        mngBoardService.deleteBoardNotice(ids);
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