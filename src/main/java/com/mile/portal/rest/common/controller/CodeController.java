package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/code")
    public ResBody listCode() {
        List<Code> codeList = codeService.listCode();
        return new ResBody(ResBody.CODE_SUCCESS, "", codeList);
    }

    @GetMapping("/code/{codeId}")
    public ResBody selectCode(@PathVariable String codeId,
                              @RequestParam(required = false, defaultValue = "") String childCode) {

        Code codeList = codeService.selectCode(codeId, childCode);
        return new ResBody(ResBody.CODE_SUCCESS, "", codeList);
    }

    @PostMapping("/code/create")
    public ResBody createCode(@Valid @RequestBody ReqCommon.Code reqCode) {
        Code code = codeService.createCode(reqCode);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PutMapping("/code/update")
    public ResBody updateCode(@Valid @RequestBody ReqCommon.Code reqCode) {
        Code code = codeService.updateCode(reqCode);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @DeleteMapping("/code/{codeId}")
    public ResBody deleteCode(@PathVariable(name = "codeId") String codeId) {
        codeService.deleteCode(codeId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}
