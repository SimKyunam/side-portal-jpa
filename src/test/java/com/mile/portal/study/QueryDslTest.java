package com.mile.portal.study;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.domain.QAccount;
import com.mile.portal.rest.common.model.domain.QCode;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.mile.portal.rest.common.model.domain.QAccount.account;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.mile.portal.rest.user.model.domain.QBoardNotice.boardNotice;
import static com.querydsl.core.types.ExpressionUtils.count;
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
        List<Account> fetch = jpaQueryFactory.selectFrom(account).fetch();
        System.out.println(fetch);
    }

    @Test
    @DisplayName("2. User 상세 조회")
    void test2() {
        QAccount user = new QAccount("user1");

        Account account = jpaQueryFactory.selectFrom(user).fetchOne();
        System.out.println(account);
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
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        IntStream.range(0, 5).forEach(i -> entityManager.persist(createNotice()));

        // 페이징 두번째 방법
        // 컨텐츠 쿼리
        List<BoardNotice> boardNoticeList = jpaQueryFactory.select(boardNotice)
                .from(boardNotice)
                .leftJoin(boardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count만 하는 쿼리
        long total = jpaQueryFactory.selectFrom(boardNotice)
                .leftJoin(boardNotice.manager, manager)
                .fetchCount();

        Page<BoardNotice> page = PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);

        assertEquals(page.getSize(), 5);
    }

    @Test
    @DisplayName("5. 서브 쿼리")
    void test5() {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        CodeDto codeDto = jpaQueryFactory
                .select(Projections.fields(CodeDto.class,
                        parent.code, parent.codeName, parent.codeValue,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(child.code))
                                        .from(child)
                                        .where(child.parent.eq(parent)),
                                "childCount")
                ))
                .from(parent)
                .where(parent.code.eq("resourceCd"))
                .fetchOne();

        assertEquals(codeDto.getChildCount(), 3);
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