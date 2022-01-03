package com.mile.portal.rest.client.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardFaqService;
import com.mile.portal.rest.common.model.domain.board.BoardFaq;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class BoardFaqService extends BaseBoardFaqService {

    private final BoardFaqRepository boardFaqRepository;

    public BoardFaqService(CodeService codeService, BoardFaqRepository boardFaqRepository) {
        super(codeService, boardFaqRepository);
        this.boardFaqRepository = boardFaqRepository;
    }

    public int updateReadCnt(Long id) {
        BoardFaq boardFaq = boardFaqRepository.findById(id).orElseThrow(ResultNotFoundException::new);
        int readCnt = boardFaq.getReadCnt() + 1;

        boardFaq.setReadCnt(readCnt);
        boardFaqRepository.save(boardFaq);
        return readCnt;
    }
}
