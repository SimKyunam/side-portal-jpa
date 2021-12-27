package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.portal.jwt.JwtTokenProvider;
import com.mile.portal.rest.common.model.enums.Authority;
import io.jsonwebtoken.Claims;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Accessors(chain = true)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @NotBlank
    @Column(length = 20)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank
    @Column(length = 100)
    private String loginPwd;

    @NotBlank
    @Column(length = 20)
    private String loginId;

    @Enumerated(EnumType.STRING)
    private Authority permission;

    @Column(length = 5)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(length = 1000)
    private String tokenId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenExprDt;

    private String lastLoginDt;
    private String lastLoginIp;
    @Column(columnDefinition = "int(11) default 0")
    private String loginFails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginExprDt;

    public Account(Claims claims) {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.convertValue(claims.get(JwtTokenProvider.USER_KEY, Map.class), Account.class);

        this.loginId = account.getLoginId();
        this.permission = account.getPermission();
        this.id = account.getId();
        this.name = account.getName();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getAuthority());
        return new UsernamePasswordAuthenticationToken(loginId, loginPwd, Collections.singleton(grantedAuthority));
    }
}
