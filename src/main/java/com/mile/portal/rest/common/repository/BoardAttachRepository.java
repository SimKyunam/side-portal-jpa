package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.board.BoardAttach;
import com.mile.portal.rest.common.model.dto.CodeNativeDto;
import com.mile.portal.rest.common.repository.custom.BoardAttachRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardAttachRepository extends JpaRepository<BoardAttach, Long>, BoardAttachRepositoryCustom {

    Optional<List<BoardAttach>> findByAttachUploadIn(List<String> attachUpload);
}
