package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.mng.model.comm.ReqManager;
import com.mile.portal.rest.mng.model.dto.ManagerQnaDto;
import com.mile.portal.rest.mng.service.MngQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mng/qna")
public class MngQnaController {
    private final MngQnaService mngQnaService;

    @GetMapping("")
    public ResBody listMngQna(@AuthenticationPrincipal Account account,
                              @RequestParam(required = false) ReqManager.Qna reqManagerQna,
                              @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ManagerQnaDto> boardNoticeList = mngQnaService.listMngQna(reqManagerQna, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @PostMapping("/create")
    public ResBody createMngQna(@AuthenticationPrincipal Account account,
                                @RequestBody @Valid ReqManager.Qna reqManagerQna) {
        mngQnaService.createMngQna(reqManagerQna);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/{id}")
    public ResBody selectMngQna(@AuthenticationPrincipal Account account,
                                @PathVariable(name = "id") Long id) {
        ManagerQnaDto managerQna = mngQnaService.selectMngQna(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", managerQna);
    }

    @PutMapping("/update")
    public ResBody updateMngQna(@AuthenticationPrincipal Account account,
                                @RequestBody @Valid ReqManager.Qna reqManagerQna) {
        mngQnaService.updateMngQna(reqManagerQna);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @DeleteMapping("/delete")
    public ResBody deleteMngQna(@AuthenticationPrincipal Account account,
                                @RequestParam String ids) {
        mngQnaService.deleteMngQna(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}
