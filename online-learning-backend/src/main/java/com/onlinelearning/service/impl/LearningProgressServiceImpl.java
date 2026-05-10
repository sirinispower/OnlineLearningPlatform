package com.onlinelearning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onlinelearning.entity.LearningProgress;
import com.onlinelearning.mapper.LearningProgressMapper;
import com.onlinelearning.service.LearningProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningProgressServiceImpl implements LearningProgressService {

    private final LearningProgressMapper learningProgressMapper;

    @Override
    public LearningProgress getProgress(Long userId, Long videoId) {
        return learningProgressMapper.selectByUserAndVideo(userId, videoId);
    }

    @Override
    public void saveProgress(LearningProgress progress) {
        learningProgressMapper.insert(progress);
    }

    @Override
    public void updateProgress(LearningProgress progress) {
        learningProgressMapper.updateById(progress);
    }
}
