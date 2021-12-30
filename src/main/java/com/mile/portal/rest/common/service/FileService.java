package com.mile.portal.rest.common.service;

import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadPath;

    //디렉토리 생성
    public void init(Path root) {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("ExceptionMsg.RUNTIME_CREATE_NOT_UPLOAD");
        }
    }

    /**
     * 오늘 날짜 폴더(yyyyMMdd)로 생성
     *
     * @param file
     * @return
     */
    public Attach store(MultipartFile file) {
        return this.store(file, null);
    }

    /**
     * childPath에 지정한 하위 폴더 생성 (default 파일명 uuid 사용)
     *
     * @param file
     * @param childPath
     * @return
     */
    public Attach store(MultipartFile file, String childPath) {
        return this.store(file, childPath, "Y");
    }

    /**
     * uuid를 사용하지 않을 경우
     *
     * @param file
     * @param childPath
     * @param useUuid
     * @return
     */
    public Attach store(MultipartFile file, String childPath, String useUuid) {
        try {
            if (file.isEmpty()) {
                throw new Exception("ExceptionMsg.EXCEPTION_FILE_EMPTY");
            }

            //하위 폴더가 없는 경우 현재날짜, 있는 경우 그 폴더 이름으로 생성
            childPath = childPath == null ? DateTimeUtil.getToday("yyyyMMdd") : childPath;
            Path root = Paths.get(uploadPath, childPath);

            //root에 폴더가 없는 경우 폴더 생성
            if (!Files.exists(root)) {
                init(root);
            }

            try (InputStream inputStream = file.getInputStream()) {
                String orgFileName = file.getOriginalFilename();
                String updFilename = useUuid.equals("Y") ? UUID.randomUUID().toString() : orgFileName;

                Files.copy(inputStream, root.resolve(updFilename),
                        StandardCopyOption.REPLACE_EXISTING);

                return new Attach()
                        .setAttachName(updFilename)
                        .setAttachUpload(updFilename)
                        .setAttachDown(orgFileName)
                        .setPath(root.toString())
                        .setSize(Files.size(root.resolve(updFilename)));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    //디폴트 경로에 모든 폴더 조회
    public Stream<Path> loadAll() {
        try {
            Path root = Paths.get(uploadPath);
            return Files.walk(root, 1)
                    .filter(path -> !path.equals(root));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    //지정 경로에 폴더 찾기
    public Path load(String path, String filename) {
        return Paths.get(path.isEmpty() ? uploadPath : path).resolve(filename);
    }

    //경로에 자원 조회
    public Resource loadAsResource(String filename) {
        return this.loadAsResource("", filename);
    }

    public Resource loadAsResource(String path, String filename) {
        try {
            Path file = load(path, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    //해당 경로에 모두 삭제
    public void deleteAll(String path) {
        FileSystemUtils.deleteRecursively(Paths.get(path).toFile());
    }

    //경로를 지정하지 않은 경우 default 경로 사용
    public void delete(String filename) {
        this.delete("", filename);
    }

    //경로에 단일 파일 삭제
    public void delete(String path, String filename) {
        path = path.equals("") ? uploadPath : path;
        FileSystemUtils.deleteRecursively(Paths.get(path).resolve(filename).toFile());
    }
}
