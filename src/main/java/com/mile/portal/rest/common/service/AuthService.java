package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ExistsDataException;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
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
import com.mile.portal.util.CommonUtil;
import com.mile.portal.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MailUtil mailUtil;
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;

    public Client createUser(ReqLogin userLogin) {
        this.existsUserCheck(userLogin);
        String email = userLogin.getEmail();
        String loginId = userLogin.getLoginId();

        Client user = new Client();
        user.setLoginId(loginId);
        user.setLoginPwd(passwordEncoder.encode(userLogin.getLoginPwd()));
        user.setName(userLogin.getUserName());
        user.setPermission(userLogin.getUserType());
        user.setStatus(userLogin.getStatus());
        user.setEmail(email);
        if (userLogin.getEmailPassYn().equals("Y")) {
            user.setEmailCheckYn("Y");
        } else {
            user.setEmailCode(accountSendEmail("????????? ?????? ??????", email, loginId));
        }

        return clientRepository.save(user);
    }

    public ReqToken loginUser(ReqCommon.UserLogin userLogin) {
        return loginProc(userLogin, Authority.ROLE_USER);
    }

    public ReqToken oAuthLogin(String loginId) {
        Account account = new Account()
                .setLoginId(loginId)
                .setLoginPwd("oAuth2 " + loginId)
                .setPermission(Authority.ROLE_USER);

        return loginService.loginAuthenticate(account);
    }

    public Manager createMng(ReqLogin userLogin) {
        this.existsUserCheck(userLogin);
        String email = userLogin.getEmail();
        String loginId = userLogin.getLoginId();

        Manager manager = new Manager();
        manager.setLoginId(loginId);
        manager.setLoginPwd(passwordEncoder.encode(userLogin.getLoginPwd()));
        manager.setName(userLogin.getUserName());
        manager.setPermission(userLogin.getUserType());
        manager.setStatus(userLogin.getStatus());
        manager.setEmail(email);
        manager.setPhone(userLogin.getPhone());
        if (userLogin.getEmailPassYn().equals("Y")) {
            manager.setEmailCheckYn("Y");
        } else {
            manager.setEmailCode(accountSendEmail("????????? ?????? ??????", email, loginId));
        }

        return managerRepository.save(manager);
    }

    public ReqToken loginMng(ReqCommon.UserLogin userLogin) {
        return loginProc(userLogin, Authority.ROLE_ADMIN);
    }

    public void existsUserCheck(ReqLogin userLogin) {
        if (userRepository.existsByLoginId(userLogin.getLoginId())) {
            throw new ExistsDataException("?????? ???????????? ?????? ???????????????.");
        } else if (userRepository.existsByEmail(userLogin.getEmail())) {
            throw new ExistsDataException("?????? ???????????? ?????? ??????????????????.");
        }
    }

    public String accountSendEmail(String title, String email, String loginId) {
        String emailCode = CommonUtil.convertToBase64(CommonUtil.createCode(10));

        Map<String, Object> mailProperty = new HashMap<>();
        mailProperty.put("emailCode", emailCode);
        mailProperty.put("loginId", CommonUtil.convertToBase64(loginId));

        mailUtil.sendTemplateMail(email, title, "?????? ?????????", mailProperty, "mail/login");
        return emailCode;
    }

    public ReqToken loginProc(ReqCommon.UserLogin userLogin, Authority authority) {
        Account account = new Account()
                .setLoginId(userLogin.getLoginId())
                .setLoginPwd(userLogin.getLoginPwd())
                .setPermission(authority);

        return loginService.loginAuthenticate(account);
    }

    public void emailCodeCheck(String loginId, String emailCode) {
        Account account = userRepository.findByLoginIdAndEmailCode(loginId, emailCode)
                .orElseThrow(() -> new ResultNotFoundException("????????? ????????? ????????? ????????? ????????????."));

        account.setEmailCheckYn("Y");
        userRepository.save(account);
    }
}
