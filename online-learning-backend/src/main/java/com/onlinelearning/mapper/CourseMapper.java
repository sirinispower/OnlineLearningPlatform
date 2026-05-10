package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT c.*, u.nickname as instructorName, cat.name as categoryName " +
            "FROM courses c " +
            "LEFT JOIN users u ON c.instructor_id = u.id " +
            "LEFT JOIN categories cat ON c.category_id = cat.id " +
            "WHERE c.deleted = 0 AND c.status = 1 " +
            "ORDER BY c.created_at DESC")
    List<Course> selectCourseList();

    @Select("SELECT c.*, u.nickname as instructorName, cat.name as categoryName " +
            "FROM courses c " +
            "LEFT JOIN users u ON c.instructor_id = u.id " +
            "LEFT JOIN categories cat ON c.category_id = cat.id " +
            "WHERE c.id = #{id} AND c.deleted = 0")
    Course selectCourseById(@Param("id") Long id);
}
