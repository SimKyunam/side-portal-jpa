package com.mile.portal.rest.mng.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MngBoardService {

    private final BoardAttachService boardAttachService; // 게시판 첨부 파일

    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Transactional(readOnly = true)
    public Page<BoardNotice> listBoardNotice(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {

        // 컨텐츠 쿼리
        List<BoardNotice> boardNoticeList = boardNoticeRepository.noticeSearchList(reqBoardNotice, pageable);

        // count 하는 쿼리
        long total = boardNoticeRepository.noticeSearchListCnt(reqBoardNotice);

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    public BoardNotice createBoardNotice(ReqBoard.BoardNotice reqBoardNotice, List<MultipartFile> files) {
        BoardNotice boardNotice = BoardNotice.builder()
                .title(reqBoardNotice.getTitle())
                .content(reqBoardNotice.getContent())
                .ntcType(reqBoardNotice.getNtcType())
                .beginDate(reqBoardNotice.getBeginDate())
                .endDate(reqBoardNotice.getEndDate())
                .hotYn(reqBoardNotice.getHotYn())
                .pubYn(reqBoardNotice.getPubYn())
                .build();

        BoardNotice notice = boardNoticeRepository.save(boardNotice);

        //첨부파일 체크
        if(files != null){
            files = files.stream()
                    .filter(n -> !Objects.equals(n.getOriginalFilename(), ""))
                    .collect(Collectors.toList());

            if(files.size() > 0){
                boardAttachService.boardAttachProcess(notice, "NTC", files);
            }
        }

        return notice;
    }

    public BoardNotice updateBoardNotice(ReqBoard.BoardNotice reqBoardNotice, List<MultipartFile> files) {
        BoardNotice boardNotice = boardNoticeRepository.findById(reqBoardNotice.getId()).orElseThrow(ResultNotFoundException::new);

        boardNotice.setTitle(reqBoardNotice.getTitle());
        boardNotice.setContent(reqBoardNotice.getContent());
        boardNotice.setNtcType(reqBoardNotice.getNtcType());
        boardNotice.setBeginDate(reqBoardNotice.getBeginDate());
        boardNotice.setEndDate(reqBoardNotice.getEndDate());
        boardNotice.setHotYn(reqBoardNotice.getHotYn());
        boardNotice.setPubYn(reqBoardNotice.getPubYn());

        BoardNotice notice = boardNoticeRepository.save(boardNotice);

        if(reqBoardNotice.getFileModifiedYn().equals("Y")) { //첨부파일을 수정한 경우
            boardAttachService.deleteBoardAttachFile(notice, "NTC", reqBoardNotice.getNameUps());
        }

        return notice;
    }

    @Transactional(readOnly = true)
    public BoardNoticeDto selectBoardNotice(Long id) {
        BoardNotice boardNotice = boardNoticeRepository.findById(id).orElseThrow(ResultNotFoundException::new);
        return boardNoticeRepository.noticeSelect(boardNotice);
    }

    public void deleteBoardNotice(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());

        boardNoticeRepository.deleteAllById(idList);
    }
}
