package com.mile.portal.rest.mng.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("A")
public class Manager extends User {

    private String phone;
    private String email;

    @Builder
    public Manager(Integer id, String name, String loginPwd,
                   String loginId, Authority type, String status,
                   String tokenId, String tokenExprDt, String lastLoginDt,
                   String lastLoginIp, String loginFails, String deleted,
                   String loginExprDt, String phone, String email) {

        super(id, name, loginPwd,
                loginId, type, status,
                tokenId, tokenExprDt, lastLoginDt,
                lastLoginIp, loginFails, deleted, loginExprDt);

        this.phone = phone;
        this.email = email;
    }
}
