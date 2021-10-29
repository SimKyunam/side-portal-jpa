package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

//    @PostMapping("/common/createUser")
//    public ResBody createUser(@AuthenticationPrincipal LoginUser loginUser, @RequestBody ReqCommBody reqCommBody){
////        commonService.createUser(reqCommBody);
//        return new ResBody(ResBody.CODE_SUCCESS, "", null);
//    }

    @PostMapping("/common/createUser")
    public ResBody createUser(@RequestBody ReqLogin userLogin){
        commonService.createUser(userLogin);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/common/loginUser")
    public ResBody loginUser(@RequestBody ReqCommon.UserLogin userLogin){
        ReqToken reqToken = commonService.loginUser(userLogin);

        return new ResBody(ResBody.CODE_SUCCESS, "", reqToken);
    }
}
