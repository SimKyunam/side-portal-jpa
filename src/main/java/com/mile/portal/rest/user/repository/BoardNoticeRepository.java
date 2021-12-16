package com.mile.portal.rest.user.repository;

import com.mile.portal.rest.user.model.domain.BoardNotice;
import com.mile.portal.rest.user.repository.custom.BoardNoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice, Long>, BoardNoticeRepositoryCustom {
}
