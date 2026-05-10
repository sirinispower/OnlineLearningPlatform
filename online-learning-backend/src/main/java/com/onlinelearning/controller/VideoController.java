package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.Video;
import com.onlinelearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/chapter/{chapterId}")
    public Result<List<Video>> getByChapterId(@PathVariable Long chapterId) {
        return Result.success(videoService.getVideosByChapterId(chapterId));
    }

    @GetMapping("/course/{courseId}")
    public Result<List<Video>> getByCourseId(@PathVariable Long courseId) {
        return Result.success(videoService.getVideosByCourseId(courseId));
    }

    @GetMapping("/{id}")
    public Result<Video> getById(@PathVariable Long id) {
        return Result.success(videoService.getVideoById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Video video) {
        videoService.saveVideo(video);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Video video) {
        video.setId(id);
        videoService.updateVideo(video);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String url = videoService.uploadVideo(file);
        return Result.success(url);
    }
}
