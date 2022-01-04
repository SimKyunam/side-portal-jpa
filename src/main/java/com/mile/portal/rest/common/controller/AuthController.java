package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.comm.ReqLogin;
import com.mile.portal.rest.common.model.comm.ReqToken;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.service.AuthService;
import com.mile.portal.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/createUser")
    public ResBody createUser(@Valid @RequestBody ReqLogin userLogin) {
        authService.createUser(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/loginUser")
    public ResBody loginUser(@Valid @RequestBody ReqCommon.UserLogin userLogin) {
        ReqToken reqToken = authService.loginUser(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", reqToken);
    }

    @PostMapping("/createMng")
    public ResBody createMng(@Valid @RequestBody ReqLogin userLogin) {
        authService.createMng(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/loginMng")
    public ResBody loginMng(@Valid @RequestBody ReqCommon.UserLogin userLogin) {
        ReqToken reqToken = authService.loginMng(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", reqToken);
    }

    @GetMapping("/getAccessToken")
    public ResBody getAccessToken() {
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/emailCodeCheck")
    public ResBody emailCodeCheck(@RequestParam String emailCode,
                                  @RequestParam String loginId,
                                  HttpServletResponse response) throws IOException {
        authService.emailCodeCheck(CommonUtil.convertToBase64Decode(loginId), emailCode);
        response.sendRedirect("https://naver.com");

        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}