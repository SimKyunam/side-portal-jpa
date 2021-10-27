package com.mile.portal.jwt;

import com.mile.portal.rest.common.model.dto.LoginUser;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private final String SECRETKEY = "12345678901234567890123456789012";
    private Key key;

    @BeforeEach
    public void init() {
        key = Keys.hmacShaKeyFor(SECRETKEY.getBytes());
    }

    @Test
    @DisplayName("1. 토큰 생성")
    void test1() {
        Date now = new Date();
        LoginUser loginUser = createLoginUser();

        JwtBuilder builder = Jwts.builder()
                .setSubject(loginUser.getId().toString())
                .claim("user", loginUser); //계정

        String token = builder
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (1000L * 60 * 60 * 12))) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        assertEquals("ssss", token);
    }

    //LoginUser 계정 생성
    LoginUser createLoginUser() {
        return LoginUser.builder()
                .loginId("testUser")
                .id(1L)
                .build();
    }

}