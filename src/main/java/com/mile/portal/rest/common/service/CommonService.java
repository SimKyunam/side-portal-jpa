package com.mile.portal.rest.common.service;

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

    public void createCode(ReqCommon.Code reqCode) {
        int depth = 1, ord = 1;
        String parentId = reqCode.getParentId();
        Code parentCode = null;

        if(parentId != null) {
            CodeDto parent = codeRepository.findParentCode(reqCode.getParentId());
            if(parent != null) {
                depth = parent.getDepth() + 1;
                ord = parent.getChildCount().intValue() + 1;

                parentCode = Code.builder()
                        .code(parent.getCode())
                        .codeValue(parent.getCodeValue())
                        .codeName(parent.getCodeName())
                        .depth(parent.getDepth())
                        .ord(parent.getOrd())
                        .build();
            }
        } else {
            long parentCnt = codeRepository.countByParentIsNull();
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

        codeRepository.save(code);
    }

    public void updateCode(ReqCommon.Code reqCode) {
    }

    public void deleteCode(String codeId) {
    }
}
