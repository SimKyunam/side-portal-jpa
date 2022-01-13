package com.mile.portal.rest.mng.repository.custom;

import com.mile.portal.rest.mng.model.comm.ReqManager;
import com.mile.portal.rest.mng.model.dto.ManagerQnaDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManagerQnaRepositoryCustom {

    List<ManagerQnaDto> mngQnaSearchList(ReqManager.Qna reqManagerQna, Pageable pageable);

    Long mngQnaSearchListCnt(ReqManager.Qna reqManagerQna);

    Optional<ManagerQnaDto> mngQnaSelect(Long id);
}
