package com.mile.portal.rest.client.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardNoticeService;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class BoardNoticeService extends BaseBoardNoticeService {

    private final BoardNoticeRepository boardNoticeRepository;

    public BoardNoticeService(CodeService codeService, BoardAttachService boardAttachService, BoardNoticeRepository boardNoticeRepository) {
        super(codeService, boardAttachService, boardNoticeRepository);
        this.boardNoticeRepository = boardNoticeRepository;
    }

    public int updateReadCnt(Long id) {
        BoardNotice boardNotice = boardNoticeRepository.findById(id).orElseThrow(ResultNotFoundException::new);
        int readCnt = boardNotice.getReadCnt() + 1;

        boardNotice.setReadCnt(readCnt);
        boardNoticeRepository.save(boardNotice);
        return readCnt;
    }
}
