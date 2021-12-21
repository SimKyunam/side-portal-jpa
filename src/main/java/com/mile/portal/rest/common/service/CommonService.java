package com.mile.portal.rest.common.service;

import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.dto.CodeDto;
import com.mile.portal.rest.common.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
