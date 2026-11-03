
package com.hona.module23.controller;

import com.hona.module23.service.FileStorageService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Upload API
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {

        String storedFilename = fileStorageService.storeFile(file);

        return "File uploaded successfully! Stored as: " + storedFilename;
    }

    // Download API
    @GetMapping("/download/{filename}")
    public ResponseEntity<StreamingResponseBody> downloadFile(
            @PathVariable String filename) throws IOException {

        Path filePath = fileStorageService.getFilePath(filename);

        StreamingResponseBody stream = outputStream -> {
            try (var inputStream = java.nio.file.Files.newInputStream(filePath)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }
}