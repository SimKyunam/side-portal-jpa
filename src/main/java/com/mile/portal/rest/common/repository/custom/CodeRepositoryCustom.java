package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CodeRepositoryCustom {
    List<Code> findTreeAll();
    Code findTreeCode(String code, String childCode);
    CodeDto findParentCode(String parentId, String codeId);
}
