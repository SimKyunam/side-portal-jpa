package com.mile.portal.rest.client.controller;

import com.mile.portal.rest.client.service.BoardQnaService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.comm.ResBody;
import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
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
@RequestMapping("/api/v1/client/board/qna")
public class BoardQnaController {

    private final BoardQnaService boardQnaService;

    @GetMapping("")
    public ResBody listBoardQna(@AuthenticationPrincipal Account account,
                                @RequestParam(required = false) ReqBoard.BoardQna reqBoardQna,
                                @PageableDefault(sort = "id", size = 100, direction = Sort.Direction.DESC) Pageable pageable) {
        Long clientId = account.getId();

        Page<BoardQnaDto> boardQnaList = boardQnaService.listBoardQna(reqBoardQna, pageable, clientId);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardQnaList);
    }

    @PostMapping("/create")
    public ResBody createBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid ReqBoard.BoardQna reqBoardQna,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        Long managerId = account.getId();

        boardQnaService.createBoardQna(reqBoardQna, files, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @PostMapping("/update")
    public ResBody updateBoardNotice(@AuthenticationPrincipal Account account,
                                     @Valid ReqBoard.BoardQna reqBoardQna,
                                     @RequestPart(required = false) List<MultipartFile> files) {
        Long managerId = account.getId();

        boardQnaService.updateBoardQna(reqBoardQna, files, managerId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }

    @GetMapping("/{id}")
    public ResBody selectBoardQna(@AuthenticationPrincipal Account account,
                                  @PathVariable(name = "id") Long id) {
        Long clientId = account.getId();

        BoardQnaDto boardQna = boardQnaService.selectBoardQna(id, clientId);
        return new ResBody(ResBody.CODE_SUCCESS, "", boardQna);
    }

    @DeleteMapping("/delete")
    public ResBody deleteBoardNotice(@AuthenticationPrincipal Account account,
                                     @RequestParam String ids) {
        Long clientId = account.getId();

        boardQnaService.deleteBoardQna(ids, clientId);
        return new ResBody(ResBody.CODE_SUCCESS, "", null);
    }
}