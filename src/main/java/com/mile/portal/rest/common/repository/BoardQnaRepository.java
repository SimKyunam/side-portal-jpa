package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.board.BoardQna;
import com.mile.portal.rest.common.repository.custom.BoardQnaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardQnaRepository extends JpaRepository<BoardQna, Long>, BoardQnaRepositoryCustom {
}
