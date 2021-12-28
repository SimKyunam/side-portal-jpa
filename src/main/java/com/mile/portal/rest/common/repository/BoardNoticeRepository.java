package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.repository.custom.BoardNoticeRepositoryCustom;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice, Long>, BoardNoticeRepositoryCustom {

}
