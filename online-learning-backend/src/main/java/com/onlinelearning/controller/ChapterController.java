package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.Chapter;
import com.onlinelearning.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/course/{courseId}")
    public Result<List<Chapter>> getByCourseId(@PathVariable Long courseId) {
        return Result.success(chapterService.getChaptersByCourseId(courseId));
    }

    @PostMapping
    public Result<?> save(@RequestBody Chapter chapter) {
        chapterService.saveChapter(chapter);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Chapter chapter) {
        chapter.setId(id);
        chapterService.updateChapter(chapter);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return Result.success();
    }
}
