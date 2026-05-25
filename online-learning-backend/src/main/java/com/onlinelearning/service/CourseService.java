package com.onlinelearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    Page<Course> getCoursePage(Integer page, Integer size, String keyword, Long categoryId);

    Course getCourseById(Long id);

    void saveCourse(Course course);

    void updateCourse(Course course);

    void deleteCourse(Long id);

    List<Course> getAllCourses();

    String uploadCover(MultipartFile file);
}