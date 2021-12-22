package com.mile.portal.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.portal.rest.common.model.domain.Account;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenProvider jwtTokenProvider) {

        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
    ) throws IOException, ServletException {
        Authentication authentication = getAuthentication(request, response);

        if (authentication != null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response){
        String headerAuth = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (headerAuth != null) { //토큰이 존재하는 경우
            String token = headerAuth.replaceAll("^Bearer( )*", "");
            if (jwtTokenProvider.validateToken(token)) {  // token 검증
                Claims claims = jwtTokenProvider.getTokenClaims(token);

                //리프레시 토큰인 경우 Access 토큰 재발급
                if (needRefresh(claims)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Account account = mapper.convertValue(claims.get(JwtTokenProvider.USER_KEY, Map.class), Account.class);

                    token = jwtTokenProvider.createToken(account);
                    response.setHeader(JwtTokenProvider.AUTHORITIES_KEY, token);
                }
            } else {
                response.setHeader(JwtTokenProvider.AUTHORITIES_KEY, "invalidation Token");
            }

            //인증 처리
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) jwtTokenProvider.getAuthentication(token);

            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            return usernamePasswordAuthenticationToken;
        }

        return null;
    }

    private boolean needRefresh(Claims claims) {
        long exp = claims.getExpiration().getTime();
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain > JwtTokenProvider.TOKEN_VALID_TIME;
        }
        return false;
    }
}
