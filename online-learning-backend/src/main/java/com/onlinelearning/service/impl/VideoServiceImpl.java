package com.onlinelearning.service.impl;

import com.onlinelearning.entity.Video;
import com.onlinelearning.mapper.VideoMapper;
import com.onlinelearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;

    @Value("${file.upload.video-path}")
    private String videoUploadPath;

    @Override
    public List<Video> getVideosByChapterId(Long chapterId) {
        return videoMapper.selectByChapterId(chapterId);
    }

    @Override
    public List<Video> getVideosByCourseId(Long courseId) {
        return videoMapper.selectByCourseId(courseId);
    }

    @Override
    public Video getVideoById(Long id) {
        return videoMapper.selectById(id);
    }

    @Override
    public void saveVideo(Video video) {
        videoMapper.insert(video);
    }

    @Override
    public void updateVideo(Video video) {
        videoMapper.updateById(video);
    }

    @Override
    public void deleteVideo(Long id) {
        videoMapper.deleteById(id);
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(videoUploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".mp4";
            String filename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());

            return "/uploads/videos/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video", e);
        }
    }
}
