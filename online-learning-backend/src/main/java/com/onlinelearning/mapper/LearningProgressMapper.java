package com.onlinelearning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.entity.LearningProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LearningProgressMapper extends BaseMapper<LearningProgress> {

    @Select("SELECT * FROM learning_progress WHERE user_id = #{userId} AND video_id = #{videoId} AND deleted = 0")
    LearningProgress selectByUserAndVideo(@Param("userId") Long userId, @Param("videoId") Long videoId);
}
