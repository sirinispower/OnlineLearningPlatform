package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

    @Select("SELECT * FROM chapters WHERE course_id = #{courseId} AND deleted = 0 ORDER BY sort ASC")
    List<Chapter> selectByCourseId(@Param("courseId") Long courseId);
}
