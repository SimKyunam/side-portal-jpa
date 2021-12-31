package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Menu;
import com.mile.portal.rest.common.model.dto.CodeNativeDto;
import com.mile.portal.rest.common.repository.custom.MenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, String>, MenuRepositoryCustom {
    long countByParentIsNullAndCodeNot(String codeId);

    @Query(nativeQuery = true)
    List<CodeNativeDto> findByCodeChild(@Param("code") String code);
}
