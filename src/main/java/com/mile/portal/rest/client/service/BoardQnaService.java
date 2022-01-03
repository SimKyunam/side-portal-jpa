package com.mile.portal.rest.client.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardQnaService;
import com.mile.portal.rest.client.repository.ClientRepository;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BoardQnaService extends BaseBoardQnaService {
    private final CodeService codeService;
    private final BoardAttachService boardAttachService;

    private final BoardQnaRepository boardQnaRepository;
    private final ClientRepository clientRepository;

    public BoardQnaService(CodeService codeService, BoardAttachService boardAttachService, BoardQnaRepository boardQnaRepository, ClientRepository clientRepository) {
        super(codeService, boardAttachService, boardQnaRepository);
        this.codeService = codeService;
        this.boardAttachService = boardAttachService;
        this.boardQnaRepository = boardQnaRepository;
        this.clientRepository = clientRepository;
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

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public void deleteBoardQna(String ids, Long clientId) {
        List<Long> boardIdList = CommonUtil.stringNotEmptyIdConvertToList(ids);

        List<BoardQna> boardQnas = boardQnaRepository.findByIdInAndClientId(boardIdList, clientId);
        boardAttachService.deleteBoardAttachFiles(boardQnas, "QNA"); // 첨부파일 삭제

        boardQnas.forEach(qna -> qna.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
