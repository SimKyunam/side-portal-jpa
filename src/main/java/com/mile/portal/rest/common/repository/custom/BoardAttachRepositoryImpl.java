package com.mile.portal.rest.common.repository.custom;

import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.rest.common.model.domain.QCode;
import com.mile.portal.rest.common.model.domain.board.Board;
import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.model.domain.board.BoardNotice;
import com.mile.portal.rest.user.model.comm.ReqBoard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.mile.portal.rest.common.model.domain.board.QBoardAttach.boardAttach;

@Repository
@RequiredArgsConstructor
public class BoardAttachRepositoryImpl implements BoardAttachRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Long deleteBoardAttachFile(Board board, List<BoardAttach> boardAttachList) {
        return query.delete(boardAttach)
                .where(boardAttach.board.eq(board), boardAttachListEq(boardAttachList)).execute();
    }

    private BooleanExpression boardAttachListEq(List<BoardAttach> boardAttachList) {
        if(boardAttachList != null && !boardAttachList.isEmpty()){
            List<String> attachUploadList = boardAttachList.stream()
                    .map(Attach::getAttachUpload)
                    .collect(Collectors.toList());

            return boardAttach.attachUpload.in(attachUploadList);
        }
        return null;
    }
}
