package com.mile.portal.rest.user.controller;

import com.mile.portal.rest.common.model.dto.ResBody;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @RequestMapping("/notice/list")
    public ResBody listBoardNotice(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardNotice> boardNoticeList = boardService.listBoardNotice(pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }
}
