package com.mile.portal.rest.mng.controller;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import com.mile.portal.rest.mng.service.MngBoardQnaService;
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
@RequestMapping("/api/v1/mng/board/qna")
public class MngBoardQnaController {

    private final MngBoardQnaService mngBoardQnaService;

    @GetMapping("")
    public ResBody listBoardQna(@AuthenticationPrincipal Account account,
                                @RequestParam(required = false) ReqBoard.BoardQna reqBoardQna,
                                @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardQnaDto> boardNoticeList = mngBoardQnaService.listBoardQna(reqBoardQna, pageable, null);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardNoticeList);
    }

    @PostMapping("/update")
    public ResBody updateBoardQna(@AuthenticationPrincipal Account account,
                                  @Valid ReqBoard.BoardQnaAnswer reqBoardQna,
                                  @RequestPart(required = false) List<MultipartFile> files) {
        Long managerId = account.getId();

        mngBoardQnaService.updateBoardQna(reqBoardQna, files, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardQna(@AuthenticationPrincipal Account account,
                                  @PathVariable(name = "id") Long id) {
        BoardQnaDto boardQna = mngBoardQnaService.selectBoardQna(id, null);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardQna);
    }

    @DeleteMapping("/delete")
    public ResBody deleteBoardQna(@AuthenticationPrincipal Account account,
                                  @RequestParam String ids) {
        mngBoardQnaService.deleteBoardQna(ids, null);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

}
