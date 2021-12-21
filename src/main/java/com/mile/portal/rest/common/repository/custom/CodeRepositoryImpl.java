package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.QCode;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<Code> findTreeAll() {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.child, child)
                .fetchJoin()
                .where(parent.parent.isNull())
                .orderBy(parent.ord.asc())
                .fetch();
    }

    @Override
    public List<Code> findTreeCode(String code, String childCod) {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.child, child)
                .fetchJoin()
                .where(parent.code.eq(code), childCodeCdEq(childCod))
                .orderBy(parent.ord.asc())
                .fetch();
    }

    private BooleanExpression childCodeCdEq(String childCode) {
        QCode child = new QCode("child");
        return childCode != null ? child.code.eq(childCode) : null;
    }
}
