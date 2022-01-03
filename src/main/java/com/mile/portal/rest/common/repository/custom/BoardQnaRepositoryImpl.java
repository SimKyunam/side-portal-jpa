package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.mile.portal.rest.client.model.domain.QClient.client;
import static com.mile.portal.rest.common.model.domain.board.QBoardAttach.boardAttach;
import static com.mile.portal.rest.common.model.domain.board.QBoardQna.boardQna;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class BoardQnaRepositoryImpl implements BoardQnaRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<BoardQnaDto> qnaSearchList(ReqBoard.BoardQna reqBoardQna, Pageable pageable, Long clientId) {
        return qnaSelectFrom()
                .innerJoin(boardQna.client, client)
                .leftJoin(boardQna.manager, manager)
                .where(clientIdEq(clientId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long qnaSearchListCnt(ReqBoard.BoardQna reqBoardQna, Long clientId) {
        return query.selectFrom(boardQna)
                .innerJoin(boardQna.client, client)
                .leftJoin(boardQna.manager, manager)
                .where(clientIdEq(clientId))
                .fetchCount();
    }

    @Override
    public Optional<BoardQnaDto> qnaSelect(Long id, Long clientId) {
        BoardQnaDto selectQna = qnaSelectFrom()
                .innerJoin(boardQna.client, client)
                .leftJoin(boardQna.manager, manager)
                .where(boardQna.id.eq(id), clientIdEq(clientId))
                .fetchOne();

        return Optional.ofNullable(selectQna);
    }

    public JPAQuery<BoardQnaDto> qnaSelectFrom() {
        return query.select(
                Projections.fields(BoardQnaDto.class,
                        boardQna.id, boardQna.title, boardQna.content, boardQna.readCnt,
                        boardQna.qnaType, boardQna.answerContent,
                        boardQna.created, boardQna.updated,
                        client.id.as("clientId"), client.name.as("clientName"),
                        manager.id.as("managerId"), manager.name.as("managerName"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(boardAttach.id))
                                        .from(boardAttach)
                                        .where(boardAttach.board.id.eq(boardQna.id)
                                                , boardAttach.boardType.eq("QNA")), "fileCnt"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(boardAttach.id))
                                        .from(boardAttach)
                                        .where(boardAttach.board.id.eq(boardQna.id)
                                                , boardAttach.boardType.eq("ANS")), "answerFileCnt")
                )
        ).from(boardQna);
    }

    private BooleanExpression clientIdEq(Long clientId) {
        return clientId != null && clientId > 0 ? boardQna.client.id.eq(clientId) : null;
    }

}
