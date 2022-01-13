package com.mile.portal.rest.mng.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.mng.model.comm.ReqManager;
import com.mile.portal.rest.mng.model.domain.ManagerQna;
import com.mile.portal.rest.mng.model.dto.ManagerQnaDto;
import com.mile.portal.rest.mng.repository.ManagerQnaRepository;
import com.mile.portal.rest.mng.repository.ManagerRepository;
import com.mile.portal.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MngQnaService {
    private final ManagerQnaRepository managerQnaRepository;
    private final ManagerRepository managerRepository;

    public Page<ManagerQnaDto> listMngQna(ReqManager.Qna reqManagerQna, Pageable pageable) {
        // 컨텐츠 쿼리
        List<ManagerQnaDto> managerQnaList = managerQnaRepository.mngQnaSearchList(reqManagerQna, pageable);

        // count 하는 쿼리
        long total = managerQnaRepository.mngQnaSearchListCnt(reqManagerQna);

        return PageableExecutionUtils.getPage(managerQnaList, pageable, () -> total);
    }

    public ManagerQna createMngQna(ReqManager.Qna reqManagerQna) {
        ManagerQna managerQna = ManagerQna.builder()
                .qnaType(reqManagerQna.getQnaType())
                .manager(managerRepository.findById(reqManagerQna.getManagerId())
                        .orElseThrow(() -> new ResultNotFoundException("관리자 계정이 존재하지 않습니다.")))
                .build();

        return managerQnaRepository.save(managerQna);
    }

    public ManagerQnaDto selectMngQna(Long id) {
        return managerQnaRepository.mngQnaSelect(id).orElseThrow(ResultNotFoundException::new);
    }

    public ManagerQna updateMngQna(ReqManager.Qna reqManagerQna) {
        ManagerQna managerQna = managerQnaRepository.findById(reqManagerQna.getId()).orElseThrow(ResultNotFoundException::new);

        managerQna.setQnaType(reqManagerQna.getQnaType());
        managerQna.setMailSendYn(reqManagerQna.getMailSendYn());
        managerQna.setManager(managerRepository.findById(reqManagerQna.getManagerId())
                .orElseThrow(() -> new ResultNotFoundException("관리자 계정이 존재하지 않습니다.")));

        return managerQnaRepository.save(managerQna);
    }

    public void deleteMngQna(String ids) {
        List<Long> managerQnaIdList = CommonUtil.stringNotEmptyIdConvertToList(ids);

        List<ManagerQna> managerQnas = managerQnaRepository.findAllById(managerQnaIdList);

        managerQnas.forEach(managerQna -> managerQna.setDeleted(LocalDateTime.now())); // 소프트 삭제
    }
}
