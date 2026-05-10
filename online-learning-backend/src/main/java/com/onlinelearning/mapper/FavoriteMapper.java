package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT f.*, c.title, c.cover, c.description " +
            "FROM favorites f " +
            "LEFT JOIN courses c ON f.course_id = c.id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.created_at DESC")
    List<Favorite> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND course_id = #{courseId}")
    Favorite selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
