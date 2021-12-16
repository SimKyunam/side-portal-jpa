package com.mile.portal.rest.user.controller;

import com.mile.portal.rest.common.model.dto.LoginUser;
import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardDto;
import com.mile.portal.rest.user.model.dto.ReqBoard;
import com.mile.portal.rest.user.service.BoardService;
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
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/notice")
    public ResBody listBoardNotice(@AuthenticationPrincipal LoginUser loginUser,
                                   @RequestParam(required = false) ReqBoard.BoardNotice reqBoardNotice,
                                   @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BoardNotice> boardNoticeList = boardService.listBoardNotice(reqBoardNotice, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @PostMapping("/notice/create")
    public ResBody createBoardNotice(@Valid @RequestBody ReqBoard.BoardNotice reqBoardNotice) {
        boardService.createBoardNotice(reqBoardNotice);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/notice/update")
    public ResBody updateBoardNotice(@Valid @RequestBody ReqBoard.BoardNotice reqBoardNotice) {
        boardService.updateBoardNotice(reqBoardNotice);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/notice/{id}")
    public ResBody selectBoardNotice(@PathVariable(name = "id") Long id) {
        BoardDto boardNotice = boardService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }

    @DeleteMapping("/notice/{ids}")
    public ResBody deleteBoardNotice(@PathVariable(name = "ids") String ids) {
        boardService.deleteBoardNotice(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}
