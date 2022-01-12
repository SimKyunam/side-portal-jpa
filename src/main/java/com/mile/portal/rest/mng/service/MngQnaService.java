package com.mile.portal.rest.mng.service;

import com.mile.portal.rest.mng.model.comm.ReqManager;
import com.mile.portal.rest.mng.model.dto.ManagerQnaDto;
import com.mile.portal.rest.mng.repository.ManagerQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MngQnaService {
    private final ManagerQnaRepository managerQnaRepository;

    public Page<ManagerQnaDto> listMngQna(ReqManager.Qna reqManagerQna, Pageable pageable) {
        return null;
    }
}
