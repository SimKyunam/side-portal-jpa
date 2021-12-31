package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.model.domain.QMenu;
import com.mile.portal.rest.common.model.dto.MenuDto;
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
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Menu> findMenuAll() {
        QMenu parent = new QMenu("parent");
        QMenu child = new QMenu("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.child, child)
                .fetchJoin()
                .where(parent.parent.isNull())
                .orderBy(parent.ord.asc(), child.ord.asc())
                .fetch();
    }

    @Override
    public Menu findMenuDetail(Long menuId, Long childMenuId) {
        QMenu parent = new QMenu("parent");
        QMenu child = new QMenu("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.child, child)
                .fetchJoin()
                .where(parent.id.eq(menuId), childMenuIdEq(childMenuId))
                .orderBy(parent.ord.asc(), child.ord.asc())
                .fetchOne();
    }

    @Override
    public MenuDto findParentMenu(Long parentId, Long childMenuId) {
        QMenu parent = new QMenu("parent");
        QMenu child = new QMenu("child");

        return query.select(
                Projections.fields(MenuDto.class,
                        parent.id, parent.menuName, parent.menuValue, parent.ord, parent.depth,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(child.id))
                                        .from(child)
                                        .where(child.parent.eq(parent), childMenuIdNe(childMenuId)),
                                "childCount")
                ))
                .from(parent)
                .where(parent.id.eq(parentId))
                .fetchOne();
    }

    private BooleanExpression childMenuIdEq(Long childMenuId) {
        QMenu child = new QMenu("child");
        return childMenuId != null && childMenuId > 0 ? child.id.eq(childMenuId) : null;
    }

    private BooleanExpression childMenuIdNe(Long childMenuId) {
        QMenu child = new QMenu("child");
        return childMenuId != null && childMenuId > 0 ? child.id.ne(childMenuId) : null;
    }
}
