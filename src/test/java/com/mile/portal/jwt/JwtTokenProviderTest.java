package com.mile.portal.jwt;

import com.mile.portal.rest.common.model.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Account account = createAccount();

        JwtBuilder builder = Jwts.builder()
                .setSubject(account.getId().toString())
                .claim("user", account); //계정

        String token = builder
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (1000L * 60 * 60 * 12))) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        assertEquals("ssss", token);
    }

    @DisplayName("2. 토큰 정보")
    @Test
    void test_2(){
        Claims body = Jwts.parserBuilder()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiQXV0aG9yaXphdGlvbiI6IlJPTEVfVVNFUiIsImV4cCI6MTYzNjQyNTg5Mn0.fvQA6aASPiGTsJ_FDCwhULW9RsztJOxpICXKYEpkgo4")
                .getBody();

        System.out.println(body.getSubject());
    }

    //Account 계정 생성
    Account createAccount() {
        return new Account().setLoginId("testUser")
                .setId(1L);
    }

}