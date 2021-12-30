package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.rest.common.service.AttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/v1/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> listCode(@PathVariable String filename,
                                             HttpServletRequest request) {

        Attach attach = attachService.downloadFile(filename);

        // Service 호출
        Resource file = attachService.loadAsResource(attach.getPath(), filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(attach.getAttachDown(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(file);
    }
}
