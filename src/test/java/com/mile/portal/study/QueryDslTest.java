package com.mile.portal.study;

import com.mile.portal.rest.common.model.domain.QUser;
import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.user.model.domain.Client;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.mile.portal.rest.common.model.domain.QUser.user;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

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
    @DisplayName("3. 페이징 쿼리 - 1")
    void test3() {

    }

    @Test
    @DisplayName("4. 페이징 쿼리 - 2")
    void test4() {

    }

}