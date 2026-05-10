package com.onlinelearning.service;

import com.onlinelearning.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    List<Video> getVideosByChapterId(Long chapterId);

    List<Video> getVideosByCourseId(Long courseId);

    Video getVideoById(Long id);

    void saveVideo(Video video);

    void updateVideo(Video video);

    void deleteVideo(Long id);

    String uploadVideo(MultipartFile file);
}
