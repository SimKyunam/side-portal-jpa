package com.mile.portal.rest.common.controller;

import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.rest.common.service.AttachService;
import com.mile.portal.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/api/v1/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename,
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

    @GetMapping("/zipDownload")
    public void zipDownload(@RequestParam Long id, @RequestParam String type, HttpServletResponse response) {
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        String zipName = DateTimeUtil.getToday("yyyyMMddHHmmss") + ".zip";

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment;filename=" + zipName);

        try {
            zipOut = new ZipOutputStream(response.getOutputStream());
            List<? extends Attach> attachFiles = attachService.listAttacheFile(id, type);

            for (Attach attachFile : attachFiles) {
                File file = new File(attachFile.getPath() + "/" + attachFile.getAttachUpload());

                zipOut.putNextEntry(new ZipEntry(attachFile.getAttachDown()));
                fis = new FileInputStream(file);
                StreamUtils.copy(fis, zipOut);

                fis.close();
                zipOut.closeEntry();
            }
            zipOut.close();
        } catch (IOException e) {
            try {
                if (fis != null) fis.close();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());/*ignore*/
            }
            try {
                zipOut.closeEntry();
                zipOut.close();
            } catch (IOException e2) {
                System.out.println(e2.getMessage());/*ignore*/
            }
        }
    }
}
