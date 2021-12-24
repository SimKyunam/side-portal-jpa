package com.mile.portal.rest.mng.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MngBoardService {

    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Transactional(readOnly = true)
    public Page<BoardNotice> listBoardNotice(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {

        // 컨텐츠 쿼리
        List<BoardNotice> boardNoticeList = boardNoticeRepository.noticeSearchList(reqBoardNotice, pageable);

        // count 하는 쿼리
        long total = boardNoticeRepository.noticeSearchListCnt(reqBoardNotice);

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    public BoardNotice createBoardNotice(ReqBoard.BoardNotice reqBoardNotice) {
        BoardNotice boardNotice = BoardNotice.builder()
                .title(reqBoardNotice.getTitle())
                .content(reqBoardNotice.getContent())
                .ntcType(reqBoardNotice.getNtcType())
                .beginDate(reqBoardNotice.getBeginDate())
                .endDate(reqBoardNotice.getEndDate())
                .hotYn(reqBoardNotice.getHotYn())
                .pubYn(reqBoardNotice.getPubYn())
                .build();

        return boardNoticeRepository.save(boardNotice);
    }

    public BoardNotice updateBoardNotice(ReqBoard.BoardNotice reqBoardNotice) {
        BoardNotice boardNotice = boardNoticeRepository.findById(reqBoardNotice.getId()).orElseThrow(ResultNotFoundException::new);
        boardNotice.setTitle(reqBoardNotice.getTitle())
                .setContent(reqBoardNotice.getContent())
                .setNtcType(reqBoardNotice.getNtcType())
                .setBeginDate(reqBoardNotice.getBeginDate())
                .setEndDate(reqBoardNotice.getEndDate())
                .setHotYn(reqBoardNotice.getHotYn())
                .setPubYn(reqBoardNotice.getPubYn());

        return boardNoticeRepository.save(boardNotice);
    }

    @Transactional(readOnly = true)
    public BoardNoticeDto selectBoardNotice(Long id) {
        BoardNotice boardNotice = boardNoticeRepository.findById(id).orElseThrow(ResultNotFoundException::new);
        return boardNoticeRepository.noticeSelect(boardNotice);
    }

    public void deleteBoardNotice(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());

        boardNoticeRepository.deleteAllById(idList);
    }
}
