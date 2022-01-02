package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.dto.board.BoardQnaDto;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardQnaRepositoryCustom {

    List<BoardQnaDto> qnaSearchList(ReqBoard.BoardQna reqBoardQna, Pageable pageable);

    Long qnaSearchListCnt(ReqBoard.BoardQna reqBoardQna);

    Optional<BoardQnaDto> qnaSelect(Long id);
}
