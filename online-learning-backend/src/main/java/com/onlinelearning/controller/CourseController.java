package com.onlinelearning.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.Course;
import com.onlinelearning.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public Result<Page<Course>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(courseService.getCoursePage(page, size, keyword, categoryId));
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Long id) {
        return Result.success(courseService.getCourseById(id));
    }

    @GetMapping("/all")
    public Result<List<Course>> getAll() {
        return Result.success(courseService.getAllCourses());
    }

    @PostMapping
    public Result<?> save(@RequestBody Course course) {
        courseService.saveCourse(course);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        courseService.updateCourse(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success();
    }
}
