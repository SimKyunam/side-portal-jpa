package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/code/{codeId}")
    public ResBody selectCode(@PathVariable String codeId,
                              @RequestParam(required = false) String childCode){
        List<Code> codeList = commonService.selectCode(codeId, childCode);
        return new ResBody(ResBody.CODE_SUCCESS, "", codeList);
    }
}
