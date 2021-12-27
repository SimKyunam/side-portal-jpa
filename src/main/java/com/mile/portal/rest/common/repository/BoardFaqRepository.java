package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.board.BoardFaq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFaqRepository extends JpaRepository<BoardFaq, Long> {
}
