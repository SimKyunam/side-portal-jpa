package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.board.Board;
import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardAttachRepositoryCustom {

    Long deleteBoardAttachFile(Board board, List<BoardAttach> boardAttachList);
}
