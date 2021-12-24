package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.repository.custom.BoardNoticeRepositoryCustom;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.mile.portal.rest.user.model.domain.QBoardNotice.boardNotice;

@Repository
@RequiredArgsConstructor
public class BoardNoticeRepositoryImpl implements BoardNoticeRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<BoardNotice> noticeSearchList(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable) {
        return query.select(boardNotice)
                .from(boardNotice)
                .leftJoin(boardNotice.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long noticeSearchListCnt(ReqBoard.BoardNotice reqBoardNotice) {
        return query.selectFrom(boardNotice)
                .leftJoin(boardNotice.manager, manager)
                .fetchCount();
    }

    @Override
    public BoardNoticeDto noticeSelect(BoardNotice reqBoardNotice) {
        Long id = reqBoardNotice.getId();

        return query.select(Projections.fields(BoardNoticeDto.class,
                boardNotice.id, boardNotice.title, boardNotice.content, boardNotice.hotYn,
                boardNotice.pubYn, boardNotice.beginDate, boardNotice.endDate, manager.name.as("managerName")
        ))
                .from(boardNotice)
                .where(boardNotice.id.eq(id))
                .leftJoin(boardNotice.manager, manager)
                .fetchOne();
    }
}
