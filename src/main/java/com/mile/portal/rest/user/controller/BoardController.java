package com.mile.portal.rest.user.controller;

import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.user.service.BoardService;
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
@RequestMapping("/api/v1/user/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/notice")
    public ResBody listBoardNotice(@AuthenticationPrincipal Account account,
                                   @RequestParam(required = false) ReqBoard.BoardNotice reqBoardNotice,
                                   @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info(String.valueOf(account));

        Page<BoardNoticeDto> boardNoticeList = boardService.listBoardNotice(reqBoardNotice, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @GetMapping("/notice/{id}")
    public ResBody selectBoardNotice(@AuthenticationPrincipal Account account,
                                     @PathVariable(name = "id") Long id) {
        BoardNoticeDto boardNotice = boardService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }
}