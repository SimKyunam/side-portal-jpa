package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.mile.portal.rest.common.model.domain.board.QBoardAttach.boardAttach;
import static com.mile.portal.rest.common.model.domain.board.QBoardNotice.boardNotice;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class BoardNoticeRepositoryImpl implements BoardNoticeRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<BoardNoticeDto> noticeSearchList(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {
        return noticeSelectFrom()
                .innerJoin(boardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long noticeSearchListCnt(ReqBoard.BoardNotice reqBoardNotice) {
        return query.selectFrom(boardNotice)
                .innerJoin(boardNotice.manager, manager)
                .fetchCount();
    }

    @Override
    public Optional<BoardNoticeDto> noticeSelect(Long id) {
        BoardNoticeDto selectNotice = noticeSelectFrom()
                .where(boardNotice.id.eq(id))
                .innerJoin(boardNotice.manager, manager)
                .fetchOne();

        return Optional.ofNullable(selectNotice);
    }

    public JPAQuery<BoardNoticeDto> noticeSelectFrom() {
        return query.select(
                Projections.fields(BoardNoticeDto.class,
                        boardNotice.id, boardNotice.title, boardNotice.content, boardNotice.readCnt,
                        boardNotice.ntcType, boardNotice.beginDate, boardNotice.endDate, boardNotice.hotYn, boardNotice.pubYn,
                        manager.id.as("managerId"), manager.name.as("managerName"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(boardAttach.id))
                                        .from(boardAttach)
                                        .where(boardAttach.board.id.eq(boardNotice.id)), "fileCnt")
                )
        ).from(boardNotice);
    }

}
