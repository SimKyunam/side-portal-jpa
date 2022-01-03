package com.mile.portal.rest.base.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BaseBoardNoticeService {
    private final CodeService codeService;
    private final BoardAttachService boardAttachService;

    private final BoardNoticeRepository boardNoticeRepository;

    public Page<BoardNoticeDto> listBoardNotice(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {
        Map<String, Code> noticeType = codeService.selectCodeMap("noticeType"); //공통코드

        // 컨텐츠 쿼리
        List<BoardNoticeDto> boardNoticeList = boardNoticeRepository.noticeSearchList(reqBoardNotice, pageable);
        boardNoticeList = boardNoticeList.stream().peek(notice -> {
            if (noticeType.containsKey(notice.getNtcType()))
                notice.setNtcTypeName(noticeType.get(notice.getNtcType()).getCodeName());
        }).collect(Collectors.toList());

        // count 하는 쿼리
        long total = boardNoticeRepository.noticeSearchListCnt(reqBoardNotice);

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    @Cacheable(value = CacheProperties.BOARD_NOTICE, key = "#id", unless = "#result == null")
    public BoardNoticeDto selectBoardNotice(Long id) {
        Map<String, Code> noticeType = codeService.selectCodeMap("noticeType"); //공통코드

        BoardNoticeDto boardNoticeDto = boardNoticeRepository.noticeSelect(id).orElseThrow(ResultNotFoundException::new);
        if (noticeType.containsKey(boardNoticeDto.getNtcType()))
            boardNoticeDto.setNtcTypeName(noticeType.get(boardNoticeDto.getNtcType()).getCodeName());

        if (boardNoticeDto.getFileCnt() > 0) {
            boardNoticeDto.setFiles(boardAttachService.listBoardAttach(id));
        }

        return boardNoticeDto;
    }
}
