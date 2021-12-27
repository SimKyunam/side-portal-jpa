package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.user.model.dto.BoardNoticeDto;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardNoticeRepositoryCustom {

    List<BoardNotice> noticeSearchList(ReqBoard.BoardNotice reqBoardNotice, Pageable pageable);
    Long noticeSearchListCnt(ReqBoard.BoardNotice reqBoardNotice);
    BoardNoticeDto noticeSelect(BoardNotice reqBoardNotice);
}
