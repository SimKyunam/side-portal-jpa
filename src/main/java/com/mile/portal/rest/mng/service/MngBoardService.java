package com.mile.portal.rest.mng.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.rest.user.model.comm.ReqBoard;
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
    private final ManagerRepository managerRepository;

    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.BOARD_NOTICE, unless = "#result == null")
    public Page<BoardNoticeDto> listBoardNotice(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {

        // 컨텐츠 쿼리
        List<BoardNoticeDto> boardNoticeList = boardNoticeRepository.noticeSearchList(reqBoardNotice, pageable);

        // count 하는 쿼리
        long total = boardNoticeRepository.noticeSearchListCnt(reqBoardNotice);

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    @CacheEvict(value = CacheProperties.BOARD_NOTICE, allEntries = true)
    public BoardNotice createBoardNotice(ReqBoard.BoardNotice reqBoardNotice, List<MultipartFile> files, Long managerId) {
        BoardNotice boardNotice = BoardNotice.builder()
                .title(reqBoardNotice.getTitle())
                .content(reqBoardNotice.getContent())
                .ntcType(reqBoardNotice.getNtcType())
                .beginDate(reqBoardNotice.getBeginDate())
                .endDate(reqBoardNotice.getEndDate())
                .hotYn(reqBoardNotice.getHotYn())
                .pubYn(reqBoardNotice.getPubYn())
                .manager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new))
                .build();

        BoardNotice notice = boardNoticeRepository.save(boardNotice);

        //첨부파일 체크
        if (files != null) {
            files = files.stream()
                    .filter(n -> !Objects.equals(n.getOriginalFilename(), ""))
                    .collect(Collectors.toList());

            if (files.size() > 0) {
                boardAttachService.boardAttachProcess(notice, "NTC", files);
            }
        }

        return notice;
    }

    @CacheEvict(value = CacheProperties.BOARD_NOTICE, allEntries = true)
    public BoardNotice updateBoardNotice(ReqBoard.BoardNotice reqBoardNotice, List<MultipartFile> files, Long managerId) {
        BoardNotice boardNotice = boardNoticeRepository.findById(reqBoardNotice.getId()).orElseThrow(ResultNotFoundException::new);

        boardNotice.setTitle(reqBoardNotice.getTitle());
        boardNotice.setContent(reqBoardNotice.getContent());
        boardNotice.setNtcType(reqBoardNotice.getNtcType());
        boardNotice.setBeginDate(reqBoardNotice.getBeginDate());
        boardNotice.setEndDate(reqBoardNotice.getEndDate());
        boardNotice.setHotYn(reqBoardNotice.getHotYn());
        boardNotice.setPubYn(reqBoardNotice.getPubYn());
        boardNotice.setManager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new));

        BoardNotice notice = boardNoticeRepository.save(boardNotice);

        if (reqBoardNotice.getFileModifiedYn().equals("Y")) { //첨부파일을 수정한 경우
            boardAttachService.deleteBoardAttachFile(notice, "NTC", reqBoardNotice.getDeleteUploadNames());

            //첨부파일 체크
            if (files != null) {
                files = files.stream()
                        .filter(n -> !Objects.equals(n.getOriginalFilename(), ""))
                        .collect(Collectors.toList());

                if (files.size() > 0) {
                    boardAttachService.boardAttachProcess(notice, "NTC", files);
                }
            }
        }

        return notice;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.BOARD_NOTICE, key = "#id", unless = "#result == null")
    public BoardNoticeDto selectBoardNotice(Long id) {
        BoardNoticeDto boardNoticeDto = boardNoticeRepository.noticeSelect(id).orElseThrow(ResultNotFoundException::new);

        if (boardNoticeDto.getFileCnt() > 0) {
            boardNoticeDto.setFiles(boardAttachService.listBoardAttach(id));
        }

        return boardNoticeDto;
    }

    @CacheEvict(value = CacheProperties.BOARD_NOTICE, allEntries = true)
    public void deleteBoardNotice(String ids) {
        List<Long> boardIdList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());

        List<BoardNotice> boardNotices = boardNoticeRepository.findAllById(boardIdList);
        boardAttachService.deleteBoardAttachFiles(boardNotices, "NTC"); // 첨부파일 삭제

        boardNotices.forEach(notice -> notice.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
