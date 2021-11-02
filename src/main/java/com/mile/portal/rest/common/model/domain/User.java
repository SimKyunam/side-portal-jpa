package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginPwd;

    private String loginId;

    @Enumerated(EnumType.STRING)
    private Authority permission;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenExprDt;

    private String lastLoginDt;
    private String lastLoginIp;
    private String loginFails;

    private String deleted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginExprDt;
}
