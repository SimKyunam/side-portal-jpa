package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.ReqCommon;
import com.mile.portal.rest.common.model.dto.ReqLogin;
import com.mile.portal.rest.common.model.dto.ReqToken;
import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.common.service.AuthService;
import com.mile.portal.rest.common.service.CommonService;
import com.mile.portal.rest.user.model.dto.ReqBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/code")
    public ResBody listCode(){
        List<Code> codeList = commonService.listCode();
        return new ResBody(ResBody.CODE_SUCCESS, "", codeList);
    }
}
