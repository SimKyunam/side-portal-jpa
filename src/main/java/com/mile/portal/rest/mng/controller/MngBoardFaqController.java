package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
import com.mile.portal.rest.mng.service.MngBoardFaqService;
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
@RequestMapping("/api/v1/mng/board/faq")
public class MngBoardFaqController {

    private final MngBoardFaqService mngBoardFaqService;

    @GetMapping("")
    public ResBody listBoardFaq(@AuthenticationPrincipal Account account,
                                @RequestParam(required = false) ReqBoard.BoardFaq reqBoardFaq,
                                @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardFaqDto> boardFaqList = mngBoardFaqService.listBoardFaq(reqBoardFaq, pageable);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardFaqList);
    }

    @PostMapping("/create")
    public ResBody createBoardFaq(@AuthenticationPrincipal Account account,
                                  @ModelAttribute @Valid ReqBoard.BoardFaq reqBoardFaq) {
        Long managerId = account.getId();

        mngBoardFaqService.createBoardFaq(reqBoardFaq, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/update")
    public ResBody updateBoardFaq(@AuthenticationPrincipal Account account,
                                  @Valid ReqBoard.BoardFaq reqBoardFaq) {
        Long managerId = account.getId();

        mngBoardFaqService.updateBoardFaq(reqBoardFaq, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardFaq(@AuthenticationPrincipal Account account,
                                  @PathVariable(name = "id") Long id) {
        BoardFaqDto boardFaq = mngBoardFaqService.selectBoardFaq(id);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardFaq);
    }

    @DeleteMapping("/delete")
    public ResBody deleteBoardFaq(@AuthenticationPrincipal Account account,
                                  @RequestParam String ids) {
        mngBoardFaqService.deleteBoardFaq(ids);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}
