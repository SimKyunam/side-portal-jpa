package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//public class LoginUser implements UserDetails {
public class LoginUser {
    // 토큰 만료일 확인
    private Integer tokenExprCalc;

    // 로그인 공통 순번 정보
    private Long id;
    private String loginId;

    @Enumerated(EnumType.STRING)
    private Authority type;

    // 로그인 사용자 기본정보
    private String icisNo;

    // 로그인 사용자 프로젝트
    //private List<LoginMappingProject> projectList;

    // 로그인 사용자 웹에서 호출한 메뉴번호
    private Long menuId = null;

    // 로그인 사용자 삭제 여부
    private String deleted;

    // 로그인 관리자 기본정보
    private String manName;

    private Long actionLogId;

    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(type.getAuthority());
        return new UsernamePasswordAuthenticationToken(loginId, password, Collections.singleton(grantedAuthority));
    }
}
