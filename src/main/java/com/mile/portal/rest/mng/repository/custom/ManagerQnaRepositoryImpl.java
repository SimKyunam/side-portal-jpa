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
                .innerJoin(managerQna.manager, manager)
                .innerJoin(createdManager).on(managerQna.createdBy.eq(createdManager.id))
                .leftJoin(updatedManager).on(managerQna.updatedBy.eq(updatedManager.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long mngQnaSearchListCnt(ReqManager.Qna reqBoardQna) {
        return query.selectFrom(managerQna)
                .innerJoin(managerQna.manager, manager)
                .innerJoin(createdManager).on(managerQna.createdBy.eq(createdManager.id))
                .leftJoin(updatedManager).on(managerQna.updatedBy.eq(updatedManager.id))
                .fetchCount();
    }

    @Override
    public Optional<ManagerQnaDto> mngQnaSelect(Long id) {
        ManagerQnaDto selectQna = mngQnaSelectFrom()
                .innerJoin(managerQna.manager, manager)
                .innerJoin(createdManager).on(managerQna.createdBy.eq(createdManager.id))
                .leftJoin(updatedManager).on(managerQna.updatedBy.eq(updatedManager.id))
                .where(managerQna.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(selectQna);
    }

    @Override
    public List<ManagerQnaDto> findAllByQnaType(String qnaType) {
        return query.select(
                Projections.fields(ManagerQnaDto.class,
                        managerQna.id, managerQna.qnaType, managerQna.mailSendYn,
                        manager.id.as("managerId"), manager.name.as("managerName"), manager.email,
                        managerQna.created, managerQna.updated
                ))
                .from(managerQna)
                .innerJoin(managerQna.manager, manager)
                .where(managerQna.qnaType.eq(qnaType), managerQna.mailSendYn.eq("Y"))
                .fetch();
    }

    public JPAQuery<ManagerQnaDto> mngQnaSelectFrom() {
        return query.select(
                Projections.fields(ManagerQnaDto.class,
                        managerQna.id, managerQna.qnaType, managerQna.mailSendYn,
                        manager.id.as("managerId"), manager.name.as("managerName"), manager.email,
                        managerQna.created, managerQna.updated,
                        createdManager.id.as("createdManagerId"), createdManager.name.as("createdManagerName"),
                        updatedManager.id.as("updatedManagerId"), updatedManager.name.as("updatedManagerName")
                )
        ).from(managerQna);
    }
}
