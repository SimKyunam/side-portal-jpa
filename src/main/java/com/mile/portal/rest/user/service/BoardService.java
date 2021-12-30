package com.mile.portal.rest.user.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Transactional(readOnly = true)
    public Page<BoardNoticeDto> listBoardNotice(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {
        // 컨텐츠 쿼리
        List<BoardNoticeDto> boardNoticeList = boardNoticeRepository.noticeSearchList(reqBoardNotice, pageable);

        // count 하는 쿼리
        long total = boardNoticeRepository.noticeSearchListCnt(reqBoardNotice);

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    @Transactional(readOnly = true)
    public BoardNoticeDto selectBoardNotice(Long id) {
        return boardNoticeRepository.noticeSelect(id).orElseThrow(ResultNotFoundException::new);
    }
}
