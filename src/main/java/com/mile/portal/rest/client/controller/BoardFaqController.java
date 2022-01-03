package com.mile.portal.rest.client.controller;

import com.mile.portal.rest.client.service.BoardFaqService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
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
@RequestMapping("/api/v1/client/board/faq")
public class BoardFaqController {

    private final BoardFaqService boardFaqService;

    @GetMapping("")
    public ResBody listBoardFaq(@AuthenticationPrincipal Account account,
                                @RequestParam(required = false) ReqBoard.BoardFaq reqBoardFaq,
                                @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardFaqDto> boardFaqList = boardFaqService.listBoardFaq(reqBoardFaq, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardFaqList);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardFaq(@AuthenticationPrincipal Account account,
                                  @PathVariable(name = "id") Long id) {
        BoardFaqDto boardFaq = boardFaqService.selectBoardFaq(id);
        boardFaq.setReadCnt(boardFaqService.updateReadCnt(id)); // 조회수 증가 (캐싱 때문에 밖에서 처리)

        return new ResBody(ResBody.CODE_SUCCESS, "", boardFaq);
    }
}