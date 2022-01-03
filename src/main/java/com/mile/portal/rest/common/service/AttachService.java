package com.mile.portal.rest.common.service;

import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseFileService;
import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.rest.common.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachService extends BaseFileService {

    private final AttachRepository attachRepository;

    //파일 다운로드
    public Attach downloadFile(String fileName) {
        return attachRepository.findByAttachUpload(fileName).orElseThrow(ResultNotFoundException::new);
    }
}
