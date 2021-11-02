package com.mile.portal.rest.user.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    @Enumerated(EnumType.STRING)
    private Authority userType;

    private String icisNo;

    private String loginId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginPwd;

    private String activeYn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenExprDt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tokenExprCalc;

    private String lastLoginDt;

    private String deleted;
}
