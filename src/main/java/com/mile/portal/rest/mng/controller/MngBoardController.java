package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.mng.service.MngBoardService;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
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
@RequestMapping("/api/v1/mng/board")
public class MngBoardController {

    private final MngBoardService mngBoardService;

    @GetMapping("/notice")
    public ResBody listBoardNotice(@AuthenticationPrincipal Account account,
                                   @RequestParam(required = false) ReqBoard.BoardNotice reqBoardNotice,
                                   @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info(String.valueOf(account));

        Page<BoardNotice> boardNoticeList = mngBoardService.listBoardNotice(reqBoardNotice, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @PostMapping("/notice/create")
    public ResBody createBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid @RequestBody ReqBoard.BoardNotice reqBoardNotice) {
        mngBoardService.createBoardNotice(reqBoardNotice);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/notice/update")
    public ResBody updateBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid @RequestBody ReqBoard.BoardNotice reqBoardNotice) {
        mngBoardService.updateBoardNotice(reqBoardNotice);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/notice/{id}")
    public ResBody selectBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "id") Long id) {
        BoardNoticeDto boardNotice = mngBoardService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }

    @DeleteMapping("/notice/{ids}")
    public ResBody deleteBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "ids") String ids) {
        mngBoardService.deleteBoardNotice(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}