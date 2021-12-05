package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.common.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/common/createUser")
//    public ResBody createUser(@AuthenticationPrincipal LoginUser loginUser, @RequestBody ReqCommBody reqCommBody){
////        commonService.createUser(reqCommBody);
//        return new ResBody(ResBody.CODE_SUCCESS, "", null);
//    }

    @PostMapping("/common/createUser")
    public ResBody createUser(@Valid @RequestBody ReqLogin userLogin){
        authService.createUser(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/common/loginUser")
    public ResBody loginUser(@Valid @RequestBody ReqCommon.UserLogin userLogin){
        ReqToken reqToken = authService.loginUser(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", reqToken);
    }

    @PostMapping("/common/createMng")
    public ResBody createMng(@Valid @RequestBody ReqLogin userLogin) {
        authService.createMng(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/common/loginMng")
    public ResBody loginMng(@Valid @RequestBody ReqCommon.UserLogin userLogin) {
        ReqToken reqToken = authService.loginMng(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", reqToken);
    }

    @GetMapping("/common/getAccessToken")
    public ResBody getAccessToken() {
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}
