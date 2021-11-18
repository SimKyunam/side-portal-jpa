package com.mile.portal.rest.user.repository;

import com.mile.portal.rest.user.model.domain.BoardFaq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFaqRepository extends JpaRepository<BoardFaq, Long> {
}
