package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.model.dto.MenuDto;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findMenuAll();

    Menu findMenuDetail(Long menuId, Long childMenuId);

    MenuDto findParentMenu(Long parentId, Long childMenuId);
}
