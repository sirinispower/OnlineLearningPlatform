package com.onlinelearning.service.impl;

import com.onlinelearning.entity.Chapter;
import com.onlinelearning.mapper.ChapterMapper;
import com.onlinelearning.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterMapper chapterMapper;

    @Override
    public List<Chapter> getChaptersByCourseId(Long courseId) {
        return chapterMapper.selectByCourseId(courseId);
    }

    @Override
    public void saveChapter(Chapter chapter) {
        chapterMapper.insert(chapter);
    }

    @Override
    public void updateChapter(Chapter chapter) {
        chapterMapper.updateById(chapter);
    }

    @Override
    public void deleteChapter(Long id) {
        chapterMapper.deleteById(id);
    }
}
