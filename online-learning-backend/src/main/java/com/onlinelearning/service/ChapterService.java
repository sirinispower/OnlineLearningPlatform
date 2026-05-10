package com.onlinelearning.service;

import com.onlinelearning.entity.Chapter;

import java.util.List;

public interface ChapterService {
    List<Chapter> getChaptersByCourseId(Long courseId);

    void saveChapter(Chapter chapter);

    void updateChapter(Chapter chapter);

    void deleteChapter(Long id);
}
