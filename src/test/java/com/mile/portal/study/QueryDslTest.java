package com.mile.portal.study;

import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.user.model.domain.QUser;
import com.mile.portal.rest.user.model.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static com.mile.portal.rest.user.model.domain.QUser.user;
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

}