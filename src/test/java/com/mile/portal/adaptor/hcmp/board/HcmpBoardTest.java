package com.mile.portal.adaptor.hcmp.board;

import com.mile.portal.adaptor.hcmp.board.dto.HcmpBoardReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HcmpBoardTest {

    @Autowired
    private HcmpBoard hcmpBoard;

    @Test
    @DisplayName("1. 게시판 조회")
    void test1() {
        System.out.println(hcmpBoard.getBoardNotices());
    }

    @Test
    @DisplayName("2. 게시판 상세조회")
    void test2() {
        System.out.println(hcmpBoard.getBoardNoticeDetail(327));
    }

    @Test
    @DisplayName("3. 게시판 등록")
    void test3() {
        HcmpBoardReq.BoardQna boardQna = HcmpBoardReq.BoardQna.builder()
                .qnaType("LGN")
                .title("게게시시판판")
                .content("내내요용")
                .build();

        hcmpBoard.createBoardQna(boardQna);
    }

}