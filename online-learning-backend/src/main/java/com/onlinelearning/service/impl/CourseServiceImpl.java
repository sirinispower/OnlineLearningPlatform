package com.onlinelearning.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.entity.Course;
import com.onlinelearning.mapper.CourseMapper;
import com.onlinelearning.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    @Value("${file.upload.cover-path}")
    private String coverUploadPath;

    @Override
    public Page<Course> getCoursePage(Integer page, Integer size, String keyword, Long categoryId) {
        Page<Course> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getStatus, 1);
        wrapper.eq(Course::getDeleted, 0);

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Course::getTitle, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(Course::getCategoryId, categoryId);
        }

        wrapper.orderByDesc(Course::getCreatedAt);
        return courseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseMapper.selectCourseById(id);
    }

    @Override
    public void saveCourse(Course course) {
        course.setStatus(1);
        course.setViewCount(0);
        courseMapper.insert(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseMapper.updateById(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseMapper.deleteById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectCourseList();
    }

    @Override
    public String uploadCover(MultipartFile file) {
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

            return "/uploads/covers/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload cover", e);
        }
    }
}