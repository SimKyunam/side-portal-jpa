package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.repository.custom.BoardNoticeRepositoryCustom;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice, Long>, BoardNoticeRepositoryCustom {
}
