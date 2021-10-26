package com.mile.portal.rest.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    // 토큰 만료일 확인
    private Integer tokenExprCalc;

    // 로그인 공통 순번 정보
    private Integer id;
    private String loginId;
    private String type;

    // 로그인 사용자 기본정보
    private String userName;
    private String icisNo;

    // 로그인 사용자 프로젝트
    //private List<LoginMappingProject> projectList;

    // 로그인 사용자 웹에서 호출한 메뉴번호
    private Integer menuId = null;

    // 로그인 사용자 삭제 여부
    private String deleted;

    // 로그인 관리자 기본정보
    private String manName;

    private Integer actionLogId;
}
