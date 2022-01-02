package com.mile.portal.rest.user.service;

import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @MockBean
    BoardNoticeRepository boardNoticeRepository;

    private ReqBoard.BoardFaq createReqNotice() {
        return ReqBoard.BoardFaq.builder()
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