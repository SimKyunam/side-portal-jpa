package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.repository.custom.MenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {
    long countByParentIsNullAndIdNot(Long menuId);
}
