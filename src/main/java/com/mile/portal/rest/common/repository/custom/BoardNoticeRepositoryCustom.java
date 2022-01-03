package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.dto.board.BoardNoticeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardNoticeRepositoryCustom {

    List<BoardNoticeDto> noticeSearchList(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable);

    Long noticeSearchListCnt(ReqBoard.BoardNotice reqBoardNotice);

    Optional<BoardNoticeDto> noticeSelect(Long id);
}
