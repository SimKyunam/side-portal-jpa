package com.mile.portal.rest.mng.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardQnaService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
public class MngBoardQnaService extends BaseBoardQnaService {
    private final BoardAttachService boardAttachService; // 게시판 첨부 파일

    private final ManagerRepository managerRepository;
    private final BoardQnaRepository boardQnaRepository;

    public MngBoardQnaService(CodeService codeService,
                              BoardAttachService boardAttachService,
                              BoardQnaRepository boardQnaRepository,
                              ManagerRepository managerRepository) {
        super(codeService, boardAttachService, boardQnaRepository);

        this.boardAttachService = boardAttachService;
        this.managerRepository = managerRepository;
        this.boardQnaRepository = boardQnaRepository;
    }

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public BoardQna updateBoardQna(ReqBoard.BoardQnaAnswer reqBoardQna, List<MultipartFile> files, Long managerId) {
        BoardQna boardQna = boardQnaRepository.findById(reqBoardQna.getId()).orElseThrow(ResultNotFoundException::new);

        boardQna.setAnswerContent(reqBoardQna.getAnswerContent());
        boardQna.setManager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new));

        BoardQna qna = boardQnaRepository.save(boardQna);

        if (reqBoardQna.getFileModifiedYn().equals("Y")) { //첨부파일을 수정한 경우
            boardAttachService.deleteBoardAttachFile(qna, "ANS", reqBoardQna.getDeleteUploadNames());
            boardAttachService.boardAttachCheckAndUseUuidProcess(qna, "ANS", files);
        }

        return qna;
    }

    @Override
    public List<BoardQna> boardDeleteProc(List<Long> boardIdList, Long clientId) {
        List<BoardQna> boardQnas = boardQnaRepository.findAllById(boardIdList);
        boardAttachService.deleteBoardAttachFiles(boardQnas, "QNA"); // 첨부파일 삭제
        boardAttachService.deleteBoardAttachFiles(boardQnas, "ANS"); // 첨부파일 삭제

        return boardQnas;
    }
}
