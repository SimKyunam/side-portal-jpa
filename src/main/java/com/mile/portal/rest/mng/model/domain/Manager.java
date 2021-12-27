package com.mile.portal.rest.mng.model.domain;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.model.domain.board.BoardFaq;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("A")
@EqualsAndHashCode(callSuper = true)
public class Manager extends Account {

    @Column(length = 20)
    private String phone;
    @Column(length = 50)
    private String email;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<BoardFaq> boardFaqs;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<BoardNotice> boardNotices;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<BoardQna> boardQnas;

    @Builder
    public Manager(Long id, String name, String loginPwd,
                   String loginId, Authority permission, String status,
                   String tokenId, String tokenExprDt, String lastLoginDt,
                   String lastLoginIp, String loginFails,
                   String loginExprDt, String phone, String email) {

        super(id, name, loginPwd,
                loginId, permission, status,
                tokenId, tokenExprDt, lastLoginDt,
                lastLoginIp, loginFails, loginExprDt);

        this.phone = phone;
        this.email = email;
    }
}
