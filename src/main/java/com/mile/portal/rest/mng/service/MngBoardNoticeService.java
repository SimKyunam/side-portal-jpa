package com.mile.portal.rest.mng.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardNoticeService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.common.repository.BoardNoticeRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MngBoardNoticeService extends BaseBoardNoticeService {
    private final BoardAttachService boardAttachService; // 게시판 첨부 파일

    private final ManagerRepository managerRepository;
    private final BoardNoticeRepository boardNoticeRepository;

    public MngBoardNoticeService(CodeService codeService,
                                 BoardAttachService boardAttachService,
                                 BoardNoticeRepository boardNoticeRepository,
                                 ManagerRepository managerRepository) {
        super(codeService, boardAttachService, boardNoticeRepository);

        this.boardAttachService = boardAttachService;
        this.managerRepository = managerRepository;
        this.boardNoticeRepository = boardNoticeRepository;
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
        boardAttachService.boardAttachCheckAndUseUuidProcess(notice, "NTC", files);

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
            boardAttachService.boardAttachCheckAndUseUuidProcess(notice, "NTC", files);
        }

        return notice;
    }

    @CacheEvict(value = CacheProperties.BOARD_NOTICE, allEntries = true)
    public void deleteBoardNotice(String ids) {
        List<Long> boardIdList = CommonUtil.stringNotEmptyIdConvertToList(ids);

        List<BoardNotice> boardNotices = boardNoticeRepository.findAllById(boardIdList);
        boardAttachService.deleteBoardAttachFiles(boardNotices, "NTC"); // 첨부파일 삭제

        boardNotices.forEach(notice -> notice.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
