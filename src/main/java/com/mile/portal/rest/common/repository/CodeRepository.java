package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.repository.custom.CodeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, String>, CodeRepositoryCustom {
}
