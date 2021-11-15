package com.mile.portal.rest.user.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("C")
public class Client extends User {

    @Column(length = 100)
    private String icisNo;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<BoardQna> boardFaqs;

    @Builder
    public Client(Long id, String name, String loginPwd,
                  String loginId, Authority type, String status,
                  String tokenId, String tokenExprDt, String lastLoginDt,
                  String lastLoginIp, String loginFails,
                  String loginExprDt, String icisNo) {

        super(id, name, loginPwd,
                loginId, type, status,
                tokenId, tokenExprDt, lastLoginDt,
                lastLoginIp, loginFails,
                loginExprDt);

        this.icisNo = icisNo;
    }
}
