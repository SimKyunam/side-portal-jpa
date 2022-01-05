package com.mile.portal.adaptor.hcmp.board;

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

}