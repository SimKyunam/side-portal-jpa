package com.mile.portal.rest.mng.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.user.model.domain.BoardFaq;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.domain.BoardQna;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("A")
public class Manager extends User {

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
                   String loginId, Authority type, String status,
                   String tokenId, String tokenExprDt, String lastLoginDt,
                   String lastLoginIp, String loginFails,
                   String loginExprDt, String phone, String email) {

        super(id, name, loginPwd,
                loginId, type, status,
                tokenId, tokenExprDt, lastLoginDt,
                lastLoginIp, loginFails, loginExprDt);

        this.phone = phone;
        this.email = email;
    }
}
