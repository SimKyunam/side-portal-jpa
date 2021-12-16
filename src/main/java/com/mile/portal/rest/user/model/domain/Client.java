package com.mile.portal.rest.user.model.domain;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("C")
@EqualsAndHashCode(callSuper = true)
public class Client extends Account {

    @Column(length = 100)
    private String icisNo;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<BoardQna> boardFaqs;

    @Builder
    public Client(Long id, String name, String loginPwd,
                  String loginId, Authority permission, String status,
                  String tokenId, String tokenExprDt, String lastLoginDt,
                  String lastLoginIp, String loginFails,
                  String loginExprDt, String icisNo) {

        super(id, name, loginPwd,
                loginId, permission, status,
                tokenId, tokenExprDt, lastLoginDt,
                lastLoginIp, loginFails,
                loginExprDt);

        this.icisNo = icisNo;
    }
}
