package com.mile.portal.rest.common.service;

import com.mile.portal.rest.client.model.domain.Client;
import com.mile.portal.rest.client.repository.ClientRepository;
import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.comm.ReqLogin;
import com.mile.portal.rest.common.model.comm.ReqToken;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.enums.Authority;
import com.mile.portal.rest.common.repository.UserRepository;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;

    public Client createUser(ReqLogin userLogin) {
        this.existsUserCheck(userLogin);

        Client user = Client.builder()
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .name(userLogin.getUserName())
                .permission(userLogin.getUserType())
                .status(userLogin.getStatus())
                .icisNo(userLogin.getIcisNo())
                .build();

        return clientRepository.save(user);
    }

    public ReqToken loginUser(ReqCommon.UserLogin userLogin) {
        return loginProc(userLogin, Authority.ROLE_USER);
    }

    public Manager createMng(ReqLogin userLogin) {
        this.existsUserCheck(userLogin);

        Manager manager = Manager.builder()
                .loginId(userLogin.getLoginId())
                .loginPwd(passwordEncoder.encode(userLogin.getLoginPwd()))
                .name(userLogin.getUserName())
                .permission(userLogin.getUserType())
                .status(userLogin.getStatus())
                .email(userLogin.getEmail())
                .phone(userLogin.getPhone())
                .build();

        return managerRepository.save(manager);
    }

    public ReqToken loginMng(ReqCommon.UserLogin userLogin) {
        return loginProc(userLogin, Authority.ROLE_ADMIN);
    }

    public void existsUserCheck(ReqLogin userLogin) {
        if (userRepository.existsByLoginId(userLogin.getLoginId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
    }

    public ReqToken loginProc(ReqCommon.UserLogin userLogin, Authority authority) {
        Account account = new Account()
                .setLoginId(userLogin.getLoginId())
                .setLoginPwd(userLogin.getLoginPwd())
                .setPermission(authority);

        return loginService.loginAuthenticate(account);
    }
}
