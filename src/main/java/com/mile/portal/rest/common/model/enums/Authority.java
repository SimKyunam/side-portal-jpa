package com.mile.portal.rest.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Authority implements GrantedAuthority {

    ROLE_GUEST("ROLE_GUEST", "손님"), ROLE_USER("ROLE_USER", "사용자"), ROLE_MANAGER("ROLE_MANAGER", "매니저"), ROLE_ADMIN("ROLE_ADMIN", "관리자"), ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN", "슈퍼 관리자");

    private String authority;
    private String name;
}
