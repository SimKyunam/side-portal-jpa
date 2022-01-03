package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardQnaRepositoryCustom {

    List<BoardQnaDto> qnaSearchList(ReqBoard.BoardQna reqBoardQna, Pageable pageable, Long clientId);

    Long qnaSearchListCnt(ReqBoard.BoardQna reqBoardQna, Long clientId);

    Optional<BoardQnaDto> qnaSelect(Long id, Long clientId);
}
