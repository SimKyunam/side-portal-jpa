package com.mile.portal.rest.mng.repository.custom;

import com.mile.portal.rest.mng.model.comm.ReqManager;
import com.mile.portal.rest.mng.model.domain.QManager;
import com.mile.portal.rest.mng.model.dto.ManagerQnaDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.mile.portal.rest.client.model.domain.QClient.client;
import static com.mile.portal.rest.common.model.domain.board.QBoardQna.boardQna;
import static com.mile.portal.rest.mng.model.domain.QManager.manager;
import static com.mile.portal.rest.mng.model.domain.QManagerQna.managerQna;

@Repository
@RequiredArgsConstructor
public class ManagerQnaRepositoryImpl implements ManagerQnaRepositoryCustom {

    private final JPAQueryFactory query;

    private final QManager createdManager = new QManager("createdManager");
    private final QManager updatedManager = new QManager("updatedManager");

    @Override
    public List<ManagerQnaDto> mngQnaSearchList(ReqManager.Qna reqBoardQna, Pageable pageable) {
        return mngQnaSelectFrom()
                .innerJoin(boardQna.client, client)
                .leftJoin(boardQna.manager, manager)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long mngQnaSearchListCnt(ReqManager.Qna reqBoardQna) {
        return query.selectFrom(boardQna)
                .innerJoin(boardQna.client, client)
                .leftJoin(boardQna.manager, manager)
                .fetchCount();
    }

    @Override
    public Optional<ManagerQnaDto> mngQnaSelect(Long id) {
        ManagerQnaDto selectQna = mngQnaSelectFrom()
                .innerJoin(managerQna.manager, manager)
                .leftJoin(boardQna.manager, manager)
                .where(boardQna.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(selectQna);
    }

    public JPAQuery<ManagerQnaDto> mngQnaSelectFrom() {
        return query.select(
                Projections.fields(ManagerQnaDto.class,
                        managerQna.id, managerQna.qnaType, managerQna.created, managerQna.updated,
                        manager.id.as("managerId"), manager.name.as("managerName"),
                        createdManager.id.as("createdManagerId"), createdManager.name.as("createdManagerName"),
                        updatedManager.id.as("updatedManagerId"), updatedManager.name.as("updatedManagerName")
                )
        ).from(managerQna);
    }
}
