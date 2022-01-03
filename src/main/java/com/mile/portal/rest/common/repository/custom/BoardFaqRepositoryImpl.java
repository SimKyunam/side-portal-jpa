package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.mile.portal.rest.common.model.domain.board.QBoardFaq.boardFaq;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;

@Repository
@RequiredArgsConstructor
public class BoardFaqRepositoryImpl implements BoardFaqRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<BoardFaqDto> faqSearchList(ReqBoard.BoardFaq reqBoardFaq, Pageable pageable) {
        return faqSelectFrom()
                .innerJoin(boardFaq.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long faqSearchListCnt(ReqBoard.BoardFaq reqBoardFaq) {
        return query.selectFrom(boardFaq)
                .innerJoin(boardFaq.manager, manager)
                .fetchCount();
    }

    @Override
    public Optional<BoardFaqDto> faqSelect(Long id) {
        BoardFaqDto selectNotice = faqSelectFrom()
                .where(boardFaq.id.eq(id))
                .innerJoin(boardFaq.manager, manager)
                .fetchOne();

        return Optional.ofNullable(selectNotice);
    }

    public JPAQuery<BoardFaqDto> faqSelectFrom() {
        return query.select(
                Projections.fields(BoardFaqDto.class,
                        boardFaq.id, boardFaq.title, boardFaq.content, boardFaq.readCnt, boardFaq.faqType,
                        boardFaq.created, boardFaq.updated,
                        manager.id.as("managerId"), manager.name.as("managerName")
                )
        ).from(boardFaq);
    }

//    public StringTemplate dateFormat(DateTimePath<LocalDateTime> qDate, String format) {
//        return Expressions.stringTemplate("FORMATDATETIME({0}, {1})", qDate, ConstantImpl.create(format));
//    }
}
