package com.mile.portal.rest.base.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
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
public class BaseBoardQnaService {
    private final CodeService codeService;
    private final BoardAttachService boardAttachService;

    private final BoardQnaRepository boardQnaRepository;

    public Page<BoardQnaDto> listBoardQna(ReqBoard.BoardQna reqBoardQna, Pageable pageable, Long clientId) {
        Map<String, Code> qnaType = codeService.selectCodeMap("qnaType"); //공통코드

        // 컨텐츠 쿼리
        List<BoardQnaDto> boardQnaList = boardQnaRepository.qnaSearchList(reqBoardQna, pageable, clientId);
        boardQnaList = boardQnaList.stream().peek(qna -> {
            if (qnaType.containsKey(qna.getQnaType()))
                qna.setQnaTypeName(qnaType.get(qna.getQnaType()).getCodeName());
        }).collect(Collectors.toList());

        // count 하는 쿼리
        long total = boardQnaRepository.qnaSearchListCnt(reqBoardQna, clientId);

        return PageableExecutionUtils.getPage(boardQnaList, pageable, () -> total);
    }

    @Cacheable(value = CacheProperties.BOARD_QNA, key = "#id + ':' + #clientId", unless = "#result == null")
    public BoardQnaDto selectBoardQna(Long id, Long clientId) {
        Map<String, Code> qnaType = codeService.selectCodeMap("qnaType"); //공통코드

        BoardQnaDto boardQnaDto = boardQnaRepository.qnaSelect(id, clientId).orElseThrow(ResultNotFoundException::new);
        if (qnaType.containsKey(boardQnaDto.getQnaType()))
            boardQnaDto.setQnaTypeName(qnaType.get(boardQnaDto.getQnaType()).getCodeName());

        //문의
        if (boardQnaDto.getFileCnt() > 0) {
            boardQnaDto.setFiles(boardAttachService.listBoardAttachAndBoardType(id, "QNA"));
        }

        //답변
        if (boardQnaDto.getAnswerFileCnt() > 0) {
            boardQnaDto.setAnswerFiles(boardAttachService.listBoardAttachAndBoardType(id, "ANS"));
        }

        return boardQnaDto;
    }
}
