package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.common.model.domain.Account;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import java.util.Collections;

@Getter
public class AccountAdaptor extends User {

    private Account account;

    public AccountAdaptor(Account account) {
        super(account.getLoginId(), account.getLoginPwd(), Collections.singleton(account.getPermission()));
        this.account = account;
    }
}
