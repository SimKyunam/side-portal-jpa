package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.model.dto.CodeNativeDto;
import com.mile.portal.rest.common.repository.custom.CodeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, String>, CodeRepositoryCustom {
    long countByParentIsNullAndCodeNot(String codeId);

    @Query(nativeQuery = true)

    List<CodeNativeDto> findByCodeChild(@Param("code") String code);

}
