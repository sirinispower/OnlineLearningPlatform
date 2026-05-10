package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("SELECT * FROM videos WHERE chapter_id = #{chapterId} AND deleted = 0 ORDER BY sort ASC")
    List<Video> selectByChapterId(@Param("chapterId") Long chapterId);

    @Select("SELECT * FROM videos WHERE course_id = #{courseId} AND deleted = 0 ORDER BY sort ASC")
    List<Video> selectByCourseId(@Param("courseId") Long courseId);
}
