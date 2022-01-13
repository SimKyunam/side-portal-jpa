package com.mile.portal.rest.mng.repository;

import com.mile.portal.rest.mng.model.domain.ManagerQna;
import com.mile.portal.rest.mng.repository.custom.ManagerQnaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerQnaRepository extends JpaRepository<ManagerQna, Long>, ManagerQnaRepositoryCustom {
}
