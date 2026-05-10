package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.LearningProgress;
import com.onlinelearning.service.LearningProgressService;
import com.onlinelearning.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class LearningProgressController {

    private final LearningProgressService learningProgressService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{videoId}")
    public Result<LearningProgress> getProgress(@PathVariable Long videoId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(learningProgressService.getProgress(userId, videoId));
    }

    @PostMapping
    public Result<?> saveProgress(@RequestBody LearningProgress progress, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        progress.setUserId(userId);

        LearningProgress existing = learningProgressService.getProgress(userId, progress.getVideoId());
        if (existing != null) {
            existing.setProgress(progress.getProgress());
            existing.setDuration(progress.getDuration());
            existing.setCompleted(progress.getCompleted());
            learningProgressService.updateProgress(existing);
        } else {
            learningProgressService.saveProgress(progress);
        }

        return Result.success();
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
