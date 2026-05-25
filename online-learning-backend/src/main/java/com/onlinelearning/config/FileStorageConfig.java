package com.onlinelearning.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@Getter
public class FileStorageConfig {

    @Value("${file.upload.base-dir:}")
    private String baseDirOverride;

    private Path uploadRoot;
    private Path videoDir;
    private Path coverDir;

    @PostConstruct
    public void init() throws IOException {
        Path projectRoot = resolveProjectRoot();
        uploadRoot = StringUtils.hasText(baseDirOverride)
                ? Paths.get(baseDirOverride).toAbsolutePath().normalize()
                : projectRoot.resolve("uploads");
        videoDir = uploadRoot.resolve("videos");
        coverDir = uploadRoot.resolve("covers");

        Files.createDirectories(videoDir);
        Files.createDirectories(coverDir);

        log.info("Upload root: {}", uploadRoot);
        log.info("Video dir: {}", videoDir);
        log.info("Cover dir: {}", coverDir);
    }

    public String getUploadRootLocation() {
        return "file:" + uploadRoot.toAbsolutePath().normalize() + "/";
    }

    private Path resolveProjectRoot() {
        Path cwd = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        if ("online-learning-backend".equals(cwd.getFileName().toString())) {
            return cwd.getParent();
        }
        return cwd;
    }
}
