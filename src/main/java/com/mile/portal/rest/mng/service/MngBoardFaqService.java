package com.mile.portal.rest.mng.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardFaqService;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.board.BoardFaq;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MngBoardFaqService extends BaseBoardFaqService {
    private final ManagerRepository managerRepository;
    private final BoardFaqRepository boardFaqRepository;

    public MngBoardFaqService(CodeService codeService, BoardFaqRepository boardFaqRepository, ManagerRepository managerRepository) {
        super(codeService, boardFaqRepository);

        this.boardFaqRepository = boardFaqRepository;
        this.managerRepository = managerRepository;
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public BoardFaq createBoardFaq(ReqBoard.BoardFaq reqBoardFaq, Long managerId) {
        BoardFaq boardFaq = BoardFaq.builder()
                .title(reqBoardFaq.getTitle())
                .content(reqBoardFaq.getContent())
                .faqType(reqBoardFaq.getFaqType())
                .manager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new))
                .build();

        return boardFaqRepository.save(boardFaq);
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public BoardFaq updateBoardFaq(ReqBoard.BoardFaq reqBoardFaq, Long managerId) {
        BoardFaq boardFaq = boardFaqRepository.findById(reqBoardFaq.getId()).orElseThrow(ResultNotFoundException::new);

        boardFaq.setTitle(reqBoardFaq.getTitle());
        boardFaq.setContent(reqBoardFaq.getContent());
        boardFaq.setFaqType(reqBoardFaq.getFaqType());
        boardFaq.setManager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new));

        return boardFaqRepository.save(boardFaq);
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public void deleteBoardFaq(String ids) {
        List<Long> boardIdList = CommonUtil.stringNotEmptyIdConvertToList(ids);

        List<BoardFaq> boardFaq = boardFaqRepository.findAllById(boardIdList);
        boardFaq.forEach(faq -> faq.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
