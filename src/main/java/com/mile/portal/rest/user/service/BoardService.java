package com.mile.portal.rest.user.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.ReqBoard;
import com.mile.portal.rest.user.repository.BoardFaqRepository;
import com.mile.portal.rest.user.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Cacheable(value = "boardCache")
    @Transactional(readOnly = true)
    public Page<BoardNotice> listBoardNotice(ReqBoard.BoardNotice boardNotice, Pageable pageable) {
        return boardNoticeRepository.findAll(pageable);
    }

    @CacheEvict(value = "boardCache")
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

    @CacheEvict(value = "boardCache")
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

    public BoardNotice selectBoardNotice(Long id) {
        return boardNoticeRepository.findById(id).orElseThrow(ResultNotFoundException::new);
    }

    @CacheEvict(value = "boardCache")
    public void deleteBoardNotice(Long id) {
        boardNoticeRepository.deleteById(id);
    }
}
