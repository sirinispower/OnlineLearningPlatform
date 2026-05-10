package com.onlinelearning.service;

import com.onlinelearning.entity.LearningProgress;

public interface LearningProgressService {
    LearningProgress getProgress(Long userId, Long videoId);

    void saveProgress(LearningProgress progress);

    void updateProgress(LearningProgress progress);
}
