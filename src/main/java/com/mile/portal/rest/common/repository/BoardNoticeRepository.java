package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.repository.custom.BoardNoticeRepositoryCustom;
import com.mile.portal.rest.user.model.domain.BoardNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice, Long>, BoardNoticeRepositoryCustom {
}
