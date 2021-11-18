package com.mile.portal.rest.user.service;

import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.repository.BoardFaqRepository;
import com.mile.portal.rest.user.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Page<BoardNotice> listBoardNotice(Pageable pageable) {
        return boardNoticeRepository.findAll(pageable);
    }
}
