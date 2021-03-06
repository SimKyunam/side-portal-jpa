package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.repository.custom.BoardAttachRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardAttachRepository extends JpaRepository<BoardAttach, Long>, BoardAttachRepositoryCustom {

    Optional<List<BoardAttach>> findByAttachUploadIn(List<String> attachUpload);

    Optional<BoardAttach> findFirstByBoardIdAndBoardType(Long boardId, String boardType);

    Optional<List<BoardAttach>> findByBoardIdAndBoardType(Long boardId, String boardType);

    Optional<List<BoardAttach>> findByBoardId(Long boardId);
}
