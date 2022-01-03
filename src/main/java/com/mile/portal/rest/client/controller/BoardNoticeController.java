package com.mile.portal.rest.client.controller;

import com.mile.portal.rest.client.service.BoardNoticeService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client/board/notice")
public class BoardNoticeController {

    private final BoardNoticeService boardNoticeService;

    @GetMapping("")
    public ResBody listBoardNotice(@AuthenticationPrincipal Account account,
                                   @RequestParam(required = false) ReqBoard.BoardNotice reqBoardNotice,
                                   @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardNoticeDto> boardNoticeList = boardNoticeService.listBoardNotice(reqBoardNotice, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "id") Long id) {
        BoardNoticeDto boardNotice = boardNoticeService.selectBoardNotice(id);
        boardNotice.setReadCnt(boardNoticeService.updateReadCnt(id)); // 조회수 증가 (캐싱 때문에 밖에서 처리)

        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }
}