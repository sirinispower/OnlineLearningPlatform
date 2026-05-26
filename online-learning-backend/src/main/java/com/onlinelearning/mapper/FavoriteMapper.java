package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT * FROM favorites WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Favorite> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM favorites WHERE user_id = #{userId} AND course_id = #{courseId}")
    Favorite selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Delete("DELETE FROM favorites WHERE course_id = #{courseId}")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
