package com.mile.portal.study;

import com.mile.portal.rest.common.model.domain.QUser;
import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.mile.portal.rest.common.model.domain.QUser.user;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.mile.portal.rest.user.model.domain.QBoardNotice.boardNotice;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class QueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("1. User 목록 조회")
    void test1() {
        List<User> fetch = jpaQueryFactory.selectFrom(user).fetch();
        System.out.println(fetch);
    }

    @Test
    @DisplayName("2. User 상세 조회")
    void test2() {
        QUser user1 = new QUser("user1");

        User user = jpaQueryFactory.selectFrom(user1).fetchOne();
        System.out.println(user);
    }

    @Test
    @DisplayName("3. 페이징 쿼리 - 한번에 쿼리")
    void test3() {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        IntStream.range(0, 5).forEach(i -> entityManager.persist(createNotice()));

        QueryResults<BoardNotice> results = jpaQueryFactory.select(boardNotice)
                .from(boardNotice)
                .leftJoin(boardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BoardNotice> boardNoticeList = results.getResults();
        long total = results.getTotal();

        PageImpl<BoardNotice> boardNotices = new PageImpl<>(boardNoticeList, pageable, total);

        assertEquals(boardNotices.getSize(), 5);
    }

    @Test
    @DisplayName("4. 페이징 쿼리 - 컨텐츠, 카운트 따로")
    void test4() {

    }

    BoardNotice createNotice() {
        return BoardNotice.builder()
                .title("타이틀" + Math.random() * 100)
                .content("내용" + Math.random() * 100)
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .pubYn("Y")
                .hotYn("N")
                .build();
    }

}