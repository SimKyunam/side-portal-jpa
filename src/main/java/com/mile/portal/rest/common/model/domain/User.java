package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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
}
