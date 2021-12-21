package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.QCode;
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
                .where(parent.parent.isNull())
                .fetch();
    }
}
