package com.mile.portal.jwt;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.comm.ReqToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 12; // 12시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    public static final String AUTHORITIES_KEY = "Authorization";
    public static final String USER_KEY = "user";
    public static final String BEARER_TYPE = "bearer";

    private Key key;

    public JwtTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Account account) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(account.getId().toString())
                .claim(AUTHORITIES_KEY, account.getPermission())
                .claim(USER_KEY, account) //계정
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Jwt Token에서 User PK 추출
    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Jwt Token에서 id 추출
    public String getTokenId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public ReqToken generateTokenDto(Authentication authentication, Account account) {
        // User -> Account 컨버팅
        Account accountInfo = new Account()
                .setId(account.getId())
                .setLoginId(account.getLoginId())
                .setName(account.getName())
                .setPermission(account.getPermission());

        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .claim(USER_KEY, accountInfo)
                .setExpiration(new Date(now + TOKEN_VALID_TIME))        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS256)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .claim(USER_KEY, accountInfo)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return ReqToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(new Date(now + TOKEN_VALID_TIME).getTime())
                .refreshToken(refreshToken)
                .build();
    }


    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(AUTHORITIES_KEY);
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = getTokenClaims(accessToken);

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(new Account(claims), "", authorities);
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String token) {
        String exceptionMessage = "";
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            exceptionMessage = "잘못된 JWT 서명입니다.";
        } catch (ExpiredJwtException e) {
            exceptionMessage = "만료된 JWT 토큰입니다.";
        } catch (UnsupportedJwtException e) {
            exceptionMessage = "지원되지 않는 JWT 토큰입니다.";
        } catch (IllegalArgumentException e) {
            exceptionMessage = "JWT 토큰이 잘못되었습니다.";
        }

        log.info(exceptionMessage);
        return false;
    }

}