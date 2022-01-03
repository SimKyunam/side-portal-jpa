package com.mile.portal.rest.client.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
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
public class BoardFaqService {
    private final CodeService codeService;

    private final BoardFaqRepository boardFaqRepository;

    public Page<BoardFaqDto> listBoardFaq(ReqBoard.BoardFaq reqBoardFaq, Pageable pageable) {
        Map<String, Code> faqType = codeService.selectCodeMap("faqType"); //공통코드

        // 컨텐츠 쿼리
        List<BoardFaqDto> boardFaqList = boardFaqRepository.faqSearchList(reqBoardFaq, pageable);
        boardFaqList = boardFaqList.stream().map(faq -> {
            if (faqType.containsKey(faq.getFaqType()))
                faq.setFaqTypeName(faqType.get(faq.getFaqType()).getCodeName());

            return faq;
        }).collect(Collectors.toList());

        // count 하는 쿼리
        long total = boardFaqRepository.faqSearchListCnt(reqBoardFaq);

        return PageableExecutionUtils.getPage(boardFaqList, pageable, () -> total);
    }

    @Cacheable(value = CacheProperties.BOARD_FAQ, key = "#id", unless = "#result == null")
    public BoardFaqDto selectBoardFaq(Long id) {
        Map<String, Code> faqType = codeService.selectCodeMap("faqType"); //공통코드

        BoardFaqDto boardFaqDto = boardFaqRepository.faqSelect(id).orElseThrow(ResultNotFoundException::new);

        if (faqType.containsKey(boardFaqDto.getFaqType()))
            boardFaqDto.setFaqTypeName(faqType.get(boardFaqDto.getFaqType()).getCodeName());

        return boardFaqDto;
    }
}
