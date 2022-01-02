package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.mng.service.MngBoardNoticeService;
import com.mile.portal.rest.user.model.comm.ReqBoard;
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
@RequestMapping("/api/v1/mng/board/notice")
public class MngBoardNoticeController {

    private final MngBoardNoticeService mngBoardNoticeService;

    @GetMapping("")
    public ResBody listBoardNotice(@AuthenticationPrincipal Account account,
                                   @RequestParam(required = false) ReqBoard.BoardNotice reqBoardNotice,
                                   @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardNoticeDto> boardNoticeList = mngBoardNoticeService.listBoardNotice(reqBoardNotice, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @PostMapping("/create")
    public ResBody createBoardNotice(@AuthenticationPrincipal Account account,
                                     @ModelAttribute @Valid ReqBoard.BoardNotice reqBoardNotice,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        Long managerId = account.getId();

        mngBoardNoticeService.createBoardNotice(reqBoardNotice, files, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/update")
    public ResBody updateBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid ReqBoard.BoardNotice reqBoardNotice,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        Long managerId = account.getId();

        mngBoardNoticeService.updateBoardNotice(reqBoardNotice, files, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "id") Long id) {
        BoardNoticeDto boardNotice = mngBoardNoticeService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }

    @DeleteMapping("/delete")
    public ResBody deleteBoardNotice(@AuthenticationPrincipal Account account,
                                     @RequestParam String ids) {
        mngBoardNoticeService.deleteBoardNotice(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}
