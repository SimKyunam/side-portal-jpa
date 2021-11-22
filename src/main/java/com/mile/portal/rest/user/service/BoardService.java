package com.mile.portal.rest.user.service;

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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Cacheable(cacheNames = "boardCache")
    @Transactional(readOnly = true)
    public Page<BoardNotice> listBoardNotice(Pageable pageable) {
        return boardNoticeRepository.findAll(pageable);
    }

    @CacheEvict(cacheNames = "boardCache")
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
}
