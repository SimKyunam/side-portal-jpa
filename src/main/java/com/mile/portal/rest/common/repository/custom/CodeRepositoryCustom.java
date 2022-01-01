package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;

import java.util.List;

public interface CodeRepositoryCustom {
    List<Code> findCodeAll();

    Code findCodeDetail(String code, String childCode);

    CodeDto findParentCode(String parentId, String codeId);

    List<CodeDto> findByCodeChildren(String... codes);
}
