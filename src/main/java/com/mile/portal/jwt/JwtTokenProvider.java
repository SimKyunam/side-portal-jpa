package com.mile.portal.jwt;

import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ReqToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final long TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 12; // 12시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    public static final String AUTHORITIES_KEY = "Authorization";
    public static final String USER_KEY = "user";
    public static final String BEARER_TYPE = "bearer";

    private Key key;

    @Value("${jwt.key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(LoginUser loginUser) {
        Date now = new Date();

        JwtBuilder builder = Jwts.builder()
                .setSubject(loginUser.getId().toString())
                .claim(USER_KEY, loginUser); //계정
        return builder
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_MILISECOND)) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Jwt Token에서 User PK 추출
    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Jwt Token에서 id 추출
    public String getTokenId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public ReqToken generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + TOKEN_VALID_MILISECOND);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS256)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return ReqToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(new Date(now + TOKEN_VALID_MILISECOND).getTime())
                .refreshToken(refreshToken)
                .build();
    }


    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(AUTHORITIES_KEY);
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = getTokenClaims(accessToken);
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new org.springframework.security.core.userdetails
                .User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}