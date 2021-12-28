package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.mng.service.MngBoardService;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

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
                                     @ModelAttribute @Valid ReqBoard.BoardNotice reqBoardNotice,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        mngBoardService.createBoardNotice(reqBoardNotice, files);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/notice/update")
    public ResBody updateBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid ReqBoard.BoardNotice reqBoardNotice,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        mngBoardService.updateBoardNotice(reqBoardNotice, files);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/notice/{id}")
    public ResBody selectBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "id") Long id) {
        BoardNoticeDto boardNotice = mngBoardService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }

    @DeleteMapping("/notice/delete")
    public ResBody deleteBoardNotice(@AuthenticationPrincipal Account account,
                                     @RequestParam String ids) {
        mngBoardService.deleteBoardNotice(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}
