package com.mile.portal.rest.mng.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager extends BaseEntity{

    @Id
    @GeneratedValue
    private Integer id;

    private String manName;
    private String manType;
    private String loginId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginPwd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loginExprDt;

    private String phone;
    private String email;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tokenExprDt;

    private String lastLoginDt;
    private String lastLoginIp;
    private String loginFails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deleted;
}
