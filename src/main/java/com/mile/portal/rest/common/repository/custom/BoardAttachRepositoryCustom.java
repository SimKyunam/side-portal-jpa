package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.board.Board;
import com.mile.portal.rest.common.model.domain.board.BoardAttach;

import java.util.List;

public interface BoardAttachRepositoryCustom {

    Long deleteBoardAttachFile(Board board, List<BoardAttach> boardAttachList);
}
