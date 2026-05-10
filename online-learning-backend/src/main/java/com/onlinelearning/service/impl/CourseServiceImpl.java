package com.onlinelearning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.entity.Course;
import com.onlinelearning.mapper.CourseMapper;
import com.onlinelearning.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

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
}
