package com.mile.portal.rest.mng.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.domain.board.BoardFaq;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
import com.mile.portal.rest.common.repository.BoardFaqRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MngBoardFaqService {
    private final ManagerRepository managerRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.BOARD_FAQ, unless = "#result == null")
    public Page<BoardFaqDto> listBoardFaq(ReqBoard.BoardFaq reqBoardFaq, Pageable pageable) {
        // TODO : queryDsl 수정

        // 컨텐츠 쿼리
        List<BoardFaqDto> boardFaqList = boardFaqRepository.faqSearchList(reqBoardFaq, pageable);

        // count 하는 쿼리
        long total = boardFaqRepository.faqSearchListCnt(reqBoardFaq);

        return PageableExecutionUtils.getPage(boardFaqList, pageable, () -> total);
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public BoardFaq createBoardFaq(ReqBoard.BoardFaq reqBoardFaq, List<MultipartFile> files, Long managerId) {
        BoardFaq boardFaq = BoardFaq.builder()
                .title(reqBoardFaq.getTitle())
                .content(reqBoardFaq.getContent())
                .faqType(reqBoardFaq.getFaqType())
                .manager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new))
                .build();

        return boardFaqRepository.save(boardFaq);
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public BoardFaq updateBoardFaq(ReqBoard.BoardFaq reqBoardFaq, List<MultipartFile> files, Long managerId) {
        BoardFaq boardFaq = boardFaqRepository.findById(reqBoardFaq.getId()).orElseThrow(ResultNotFoundException::new);

        boardFaq.setTitle(reqBoardFaq.getTitle());
        boardFaq.setContent(reqBoardFaq.getContent());
        boardFaq.setFaqType(reqBoardFaq.getFaqType());
        boardFaq.setManager(managerRepository.findById(managerId).orElseThrow(ResultNotFoundException::new));

        return boardFaqRepository.save(boardFaq);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheProperties.BOARD_FAQ, key = "#id", unless = "#result == null")
    public BoardFaqDto selectBoardFaq(Long id) {
        // TODO : queryDsl 수정

        BoardFaqDto boardFaqDto = boardFaqRepository.faqSelect(id).orElseThrow(ResultNotFoundException::new);

        return boardFaqDto;
    }

    @CacheEvict(value = CacheProperties.BOARD_FAQ, allEntries = true)
    public void deleteBoardFaq(String ids) {
        List<Long> boardIdList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());

        List<BoardFaq> boardFaq = boardFaqRepository.findAllById(boardIdList);
        boardFaq.forEach(faq -> faq.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
