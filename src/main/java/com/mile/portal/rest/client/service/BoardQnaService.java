package com.mile.portal.rest.client.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.client.repository.ClientRepository;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardQnaService {
    private final CodeService codeService;
    private final BoardAttachService boardAttachService;

    private final BoardQnaRepository boardQnaRepository;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<BoardQnaDto> listBoardQna(ReqBoard.BoardQna reqBoardQna, Pageable pageable, Long clientId) {
        Map<String, Code> qnaType = codeService.selectCodeMap("qnaType"); //공통코드

        // 컨텐츠 쿼리
        List<BoardQnaDto> boardQnaList = boardQnaRepository.qnaSearchList(reqBoardQna, pageable, clientId);
        boardQnaList = boardQnaList.stream().map(qna -> {
            if (qnaType.containsKey(qna.getQnaType()))
                qna.setQnaTypeName(qnaType.get(qna.getQnaType()).getCodeName());

            return qna;
        }).collect(Collectors.toList());

        // count 하는 쿼리
        long total = boardQnaRepository.qnaSearchListCnt(reqBoardQna, clientId);

        return PageableExecutionUtils.getPage(boardQnaList, pageable, () -> total);
    }

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public BoardQna createBoardQna(ReqBoard.BoardQna reqBoardQna, List<MultipartFile> files, Long clientId) {
        BoardQna boardQna = BoardQna.builder()
                .title(reqBoardQna.getTitle())
                .content(reqBoardQna.getContent())
                .qnaType(reqBoardQna.getQnaType())
                .client(clientRepository.findById(clientId).orElseThrow(ResultNotFoundException::new))
                .build();

        BoardQna qna = boardQnaRepository.save(boardQna);

        //첨부파일 체크
        if (files != null) {
            files = files.stream()
                    .filter(n -> !Objects.equals(n.getOriginalFilename(), ""))
                    .collect(Collectors.toList());

            if (files.size() > 0) {
                boardAttachService.boardAttachProcess(qna, "QNA", files);
            }
        }

        return qna;
    }

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public BoardQna updateBoardQna(ReqBoard.BoardQna reqBoardQna, List<MultipartFile> files, Long clientId) {
        BoardQna boardQna = boardQnaRepository.findById(reqBoardQna.getId()).orElseThrow(ResultNotFoundException::new);

        boardQna.setTitle(reqBoardQna.getTitle());
        boardQna.setContent(reqBoardQna.getContent());
        boardQna.setQnaType(reqBoardQna.getQnaType());
        boardQna.setClient(clientRepository.findById(clientId).orElseThrow(ResultNotFoundException::new));

        BoardQna qna = boardQnaRepository.save(boardQna);

        if (reqBoardQna.getFileModifiedYn().equals("Y")) { //첨부파일을 수정한 경우
            boardAttachService.deleteBoardAttachFile(qna, "QNA", reqBoardQna.getDeleteUploadNames());

            //첨부파일 체크
            if (files != null) {
                files = files.stream()
                        .filter(n -> !Objects.equals(n.getOriginalFilename(), ""))
                        .collect(Collectors.toList());

                if (files.size() > 0) {
                    boardAttachService.boardAttachProcess(qna, "QNA", files);
                }
            }
        }

        return qna;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.BOARD_QNA, key = "#id", unless = "#result == null")
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

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public void deleteBoardQna(String ids, Long clientId) {
        List<Long> boardIdList = CommonUtil.stringNotEmptyIdConvertToList(ids);

        List<BoardQna> boardQnas = boardQnaRepository.findByIdInAndClientId(boardIdList, clientId);
        boardAttachService.deleteBoardAttachFiles(boardQnas, "QNA"); // 첨부파일 삭제

        boardQnas.forEach(qna -> qna.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
