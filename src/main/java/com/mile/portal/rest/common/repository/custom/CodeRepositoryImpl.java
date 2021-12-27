package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.QCode;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepositoryCustom {

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
                .orderBy(parent.ord.asc(), child.ord.asc())
                .fetch();
    }

    @Override
    public Code findTreeCode(String code, String childCode) {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.child, child)
                .fetchJoin()
                .where(parent.code.eq(code), childCodeCdEq(childCode))
                .orderBy(parent.ord.asc(), child.ord.asc())
                .fetchOne();
    }

    @Override
    public CodeDto findParentCode(String parentId, String codeId) {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        return query.select(
                Projections.fields(CodeDto.class,
                        parent.code, parent.codeName, parent.codeValue, parent.ord, parent.depth,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(child.code))
                                        .from(child)
                                        .where(child.parent.eq(parent), childCodeCdNe(codeId)),
                                "childCount")
                ))
                .from(parent)
                .where(parent.code.eq(parentId))
                .fetchOne();
    }

    @Override
    public List<CodeDto> findByCodeChildren(String[] codes) {
        QCode parent = new QCode("parent");
        QCode child = new QCode("child");

        return query.select(
                Projections.fields(CodeDto.class,
                        parent.code, parent.codeName, parent.codeValue, parent.ord, parent.depth,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(child.code))
                                        .from(child)
                                        .where(child.parent.eq(parent)),
                                "childCount")
                ))
                .from(parent)
                .where(parent.code.in(codes)).fetch();
    }

    private BooleanExpression childCodeCdEq(String childCode) {
        QCode child = new QCode("child");
        return childCode != null && !childCode.isEmpty() ? child.code.eq(childCode) : null;
    }

    private BooleanExpression childCodeCdNe(String childCode) {
        QCode child = new QCode("child");
        return childCode != null && !childCode.isEmpty() ? child.code.ne(childCode) : null;
    }
}
