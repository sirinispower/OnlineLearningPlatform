package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.username, u.avatar " +
            "FROM comments c " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "WHERE c.course_id = #{courseId} AND c.deleted = 0 " +
            "ORDER BY c.created_at DESC")
    List<Comment> selectByCourseId(@Param("courseId") Long courseId);
}
