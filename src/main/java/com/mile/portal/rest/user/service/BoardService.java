package com.mile.portal.rest.user.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.mng.model.domain.QManager;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.domain.QBoardNotice;
import com.mile.portal.rest.user.model.dto.ReqBoard;
import com.mile.portal.rest.user.repository.BoardFaqRepository;
import com.mile.portal.rest.user.repository.BoardNoticeRepository;
import com.mile.portal.rest.user.repository.BoardQnaRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final JPAQueryFactory jpaQueryFactory;

    private final BoardNoticeRepository boardNoticeRepository;
    private final BoardQnaRepository boardQnaRepository;
    private final BoardFaqRepository boardFaqRepository;

    @Cacheable(value = "boardCache", key = "#pageable.pageNumber")
    @Transactional(readOnly = true)
    public Page<BoardNotice> listBoardNotice(ReqBoard.BoardNotice boardNotice, Pageable pageable) {
        QBoardNotice qBoardNotice = QBoardNotice.boardNotice;
        QManager manager = QManager.manager;

        // 페이징 첫번째 방법
        // 페이징 데이터가 많지않거나 접속량이 중요하지 않은 곳이라면  해당 방법을 사용해도 문제 없음
        /*
        QueryResults<BoardNotice> results = jpaQueryFactory.select(qBoardNotice)
                .from(qBoardNotice)
                .leftJoin(qBoardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BoardNotice> boardNoticeList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(boardNoticeList, pageable, total);
        */

        // 페이징 두번째 방법
        // 컨텐츠 쿼리
        List<BoardNotice> boardNoticeList = jpaQueryFactory.select(qBoardNotice)
                .from(qBoardNotice)
                .leftJoin(qBoardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count만 하는 쿼리
        long total = jpaQueryFactory.selectFrom(qBoardNotice)
                .leftJoin(qBoardNotice.manager, manager)
                .fetchCount();

        return PageableExecutionUtils.getPage(boardNoticeList, pageable, () -> total);
    }

    @CacheEvict(value = "boardCache", allEntries = true)
    public BoardNotice createBoardNotice(ReqBoard.BoardNotice reqBoardNotice) {
        BoardNotice boardNotice = BoardNotice.builder()
                .title(reqBoardNotice.getTitle())
                .content(reqBoardNotice.getContent())
                .ntcType(reqBoardNotice.getNtcType())
                .beginDate(reqBoardNotice.getBeginDate())
                .endDate(reqBoardNotice.getEndDate())
                .hotYn(reqBoardNotice.getHotYn())
                .pubYn(reqBoardNotice.getPubYn())
                .build();

        return boardNoticeRepository.save(boardNotice);
    }

    @CacheEvict(value = "boardCache")
    public BoardNotice updateBoardNotice(ReqBoard.BoardNotice reqBoardNotice) {
        BoardNotice boardNotice = boardNoticeRepository.findById(reqBoardNotice.getId()).orElseThrow(ResultNotFoundException::new);
        boardNotice.setTitle(reqBoardNotice.getTitle())
                .setContent(reqBoardNotice.getContent())
                .setNtcType(reqBoardNotice.getNtcType())
                .setBeginDate(reqBoardNotice.getBeginDate())
                .setEndDate(reqBoardNotice.getEndDate())
                .setHotYn(reqBoardNotice.getHotYn())
                .setPubYn(reqBoardNotice.getPubYn());

        return boardNoticeRepository.save(boardNotice);
    }

    public BoardNotice selectBoardNotice(Long id) {
        return boardNoticeRepository.findById(id).orElseThrow(ResultNotFoundException::new);
    }

    @CacheEvict(value = "boardCache")
    public void deleteBoardNotice(Long id) {
        boardNoticeRepository.deleteById(id);
    }
}
