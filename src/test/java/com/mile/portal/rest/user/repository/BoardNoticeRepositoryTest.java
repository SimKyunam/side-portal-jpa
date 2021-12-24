package com.mile.portal.rest.user.repository;

import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardNoticeRepositoryTest {
    @Autowired
    private BoardNoticeRepository boardNoticeRepository;

    @Test
    @DisplayName("1. 공지사항 글 등록 테스트")
    void test1() {
        BoardNotice boardNotice = BoardNotice.builder()
                .title(createString(50))
                .pubYn("Y")
                .hotYn("N")
                .ntcType("SVC")
                .content(createString(5000))
                .build();

        BoardNotice saveNotice = boardNoticeRepository.save(boardNotice);

        assertEquals(boardNotice.getTitle(), saveNotice.getTitle());
    }

    String createString(int strLength) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < strLength; i++) {
            stringBuilder.append((char) (random.nextInt(26) + 'a'));
        }

        return stringBuilder.toString();
    }

}