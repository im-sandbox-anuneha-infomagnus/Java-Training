package com.hona.module23.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path storageLocation =
            Path.of("uploads").toAbsolutePath().normalize();

    public String storeFile(MultipartFile file) throws IOException {

        // 1. Check whether the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // 2. Check file size (maximum 10 MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 10 MB");
        }

        // 3. Get the original filename
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Invalid filename");
        }

        // 4. Extract file extension
        String extension = originalFilename.substring(
                originalFilename.lastIndexOf(".")
        );

        // 5. Generate a unique filename
        String storedFilename = UUID.randomUUID() + extension;

        // 6. Create storage directory if it doesn't exist
        Files.createDirectories(storageLocation);

        // 7. Save the file
        Path destination = storageLocation.resolve(storedFilename);

        file.transferTo(destination);

        return storedFilename;
    }
    public Path getFilePath(String filename) throws IOException {

    Path filePath = storageLocation.resolve(filename).normalize();

    if (!filePath.startsWith(storageLocation)
            || !Files.isRegularFile(filePath)) {
        throw new IOException("File not found");
    }

    return filePath;
}
}