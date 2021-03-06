package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.dto.board.BoardFaqDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardFaqRepositoryCustom {

    List<BoardFaqDto> faqSearchList(ReqBoard.BoardFaq reqBoardFaq, Pageable pageable);

    Long faqSearchListCnt(ReqBoard.BoardFaq reqBoardFaq);

    Optional<BoardFaqDto> faqSelect(Long id);
}
