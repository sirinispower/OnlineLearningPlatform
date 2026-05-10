package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload.cover-path}")
    private String coverUploadPath;

    @PostMapping("/cover")
    public Result<String> uploadCover(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadDir = Paths.get(coverUploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());

            return Result.success("/uploads/covers/" + filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload cover", e);
        }
    }
}
