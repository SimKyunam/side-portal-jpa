package com.mile.portal.rest.user.controller;

import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.ReqBoard;
import com.mile.portal.rest.user.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/notice/list")
    public ResBody listBoardNotice(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardNotice> boardNoticeList = boardService.listBoardNotice(pageable);
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

    @GetMapping("/notice/select/{id}")
    public ResBody selectBoardNotice(@PathVariable(name = "id") Long id) {
        BoardNotice boardNotice = boardService.selectBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNotice);
    }

    @DeleteMapping("/notice/delete/{id}")
    public ResBody deleteBoardNotice(@PathVariable(name = "id") Long id) {
        boardService.deleteBoardNotice(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}
