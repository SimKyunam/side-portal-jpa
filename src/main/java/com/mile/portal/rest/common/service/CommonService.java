package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.common.model.comm.ReqCommon;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {
    
    private final CodeRepository codeRepository;

    public List<Code> listCode() {
        return codeRepository.findTreeAll();
    }

    public List<Code> selectCode(String codeId, String childCode) {
        return codeRepository.findTreeCode(codeId, childCode);
    }

    public Code createCode(ReqCommon.Code reqCode) {
        int depth = 1, ord = 1;
        String parentId = reqCode.getParentId();
        String codeId = reqCode.getCodeId();
        Code parentCode = null;

        if(parentId != null) {
            CodeDto parent = Optional.ofNullable(codeRepository.findParentCode(parentId, codeId)).orElseThrow(ResultNotFoundException::new);

            depth = parent.getDepth() + 1;
            ord = parent.getChildCount().intValue() + 1;

            parentCode = Code.builder()
                    .code(parent.getCode())
                    .codeValue(parent.getCodeValue())
                    .codeName(parent.getCodeName())
                    .depth(parent.getDepth())
                    .ord(parent.getOrd())
                    .build();
        } else {
            long parentCnt = codeRepository.countByParentIsNullAndCodeNot(codeId);
            ord = (int) parentCnt + 1;
        }

        Code code = Code.builder()
                .code(reqCode.getCodeId())
                .codeName(reqCode.getCodeName())
                .codeValue(reqCode.getCodeValue())
                .depth(depth)
                .ord(ord)
                .parent(parentCode)
                .build();

        return codeRepository.save(code);
    }

    public Code updateCode(ReqCommon.Code reqCode) {
        String codeName = reqCode.getCodeName();
        String codeValue = reqCode.getCodeValue();
        String codeId = reqCode.getCodeId();

        Code code = codeRepository.findById(codeId).orElseThrow(ResultNotFoundException::new);
        code.setCodeName(codeName);
        code.setCodeValue(codeValue);

        return codeRepository.save(code);
    }

    public void deleteCode(String codeId) {
        codeRepository.deleteById(codeId);
    }
}
