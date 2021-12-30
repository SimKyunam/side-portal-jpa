package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.domain.board.Board;
import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.repository.BoardAttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardAttachService extends FileService {

    private final BoardAttachRepository boardAttachRepository;

    /** 첨부파일 */
    /**
     * 첨부파일 공통 처리 uuid 기본 사용 메소드
     *
     * @param board
     * @param boardType
     * @param fileList
     * @return
     */
    public List<BoardAttach> boardAttachProcess(Board board, String boardType, List<MultipartFile> fileList) {
        return boardAttachProcess(board, boardType, fileList, "Y");
    }

    /**
     * 첨부파일 공통 처리 uuid 설정 메소드
     *
     * @param board
     * @param boardType
     * @param fileList
     * @param useUuid
     * @return
     */
    @Transactional
    public List<BoardAttach> boardAttachProcess(Board board, String boardType, List<MultipartFile> fileList, String useUuid) {
        return fileList
                .stream()
                .map(file -> super.store(file, boardType + "_" + board.getId(), useUuid)) //물리 파일 저장
                .map(fileInfo -> {
                    BoardAttach boardAttach = BoardAttach.builder()
                            .board(board)
                            .boardType(boardType)
                            .build();
                    boardAttach.setAttachParam(fileInfo);
                    return this.createBoardAttachFile(boardAttach); // DB에 데이터 저장
                })
                .collect(Collectors.toList());
    }

    //첨부파일 생성
    public BoardAttach createBoardAttachFile(BoardAttach boardAttach) {
        return boardAttachRepository.save(boardAttach);
    }

    // 첨부파일 단일 삭제
    public Long deleteBoardAttachFile(Board board, String boardType) {
        return deleteBoardAttachFile(board, boardType, new ArrayList<>());
    }

    // 첨부파일 DB, 파일 삭제 프로세스
    @Transactional
    public Long deleteBoardAttachFile(Board board, String boardType, List<String> deleteUploadNames) {
        Long result = 0L;
        String attachPath = "";

        List<BoardAttach> deleteNameUps = boardAttachRepository.findByAttachUploadIn(deleteUploadNames)
                .orElseGet(Collections::emptyList);

        if (deleteNameUps.size() > 0) {
            attachPath = deleteNameUps.get(0).getPath();
        } else {
            attachPath = boardAttachRepository.findFirstByBoardIdAndBoardType(board.getId(), boardType)
                    .orElseGet(BoardAttach::new)
                    .getPath();
        }

        if (attachPath != null && !attachPath.isEmpty()) {
            result = boardAttachRepository.deleteBoardAttachFile(board, deleteNameUps); //데이터베이스 삭제
            if (result > 0) {
                if (deleteNameUps.size() > 0) {
                    for (BoardAttach boardAttach : deleteNameUps) {
                        super.delete(attachPath, boardAttach.getAttachUpload()); //실제 파일 단일 삭제
                    }
                } else {
                    super.deleteAll(attachPath); //실제 파일 삭제
                }
            }
        }

        return result;
    }

    //첨부 파일들 복수 DB, 파일 삭제
    @Transactional
    public int deleteBoardAttachFiles(List<? extends Board> boardList, String bbsType) {
        int deleteCnt = 0;
        for (Board board : boardList) {
            deleteCnt += this.deleteBoardAttachFile(board, bbsType);
        }

        return deleteCnt;
    }

    public List<BoardAttach> listBoardAttach(Long id) {
        return boardAttachRepository.findByBoardId(id).orElseThrow(ResultNotFoundException::new);
    }
}
