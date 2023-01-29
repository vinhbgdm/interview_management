package com.fa.ims.controller;

import com.fa.ims.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RestController
public class FileController {

    private final FileStorageService fileStorageService;
    private final CandidateService candidateService;
    public FileController(FileStorageService fileStorageService, CandidateService candidateService) {
        this.fileStorageService = fileStorageService;
        this.candidateService = candidateService;
    }

    @Value("${app.file.location.cv}")
    String fileLocation;

    @GetMapping("/files/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request)
            throws IOException {
        String relativePath = request.getRequestURL().toString().split("files/")[1];
        Resource resource = fileStorageService.loadFileAsResource(relativePath);

        String mimeType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFile().getName())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }

    @DeleteMapping("/files/{relativeFolderPath}/{relativeFilePath}")
    public ResponseEntity<?> deleteFile(@PathVariable String relativeFolderPath, @PathVariable String relativeFilePath) {
        String relativeFullPathFile = relativeFolderPath + "\\" + relativeFilePath;
        try {
            FileUtils.delete(new File(fileLocation + "\\" + relativeFullPathFile));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        candidateService.updateNullAttachFile(relativeFullPathFile);
        return ResponseEntity.noContent().build();
    }
}
