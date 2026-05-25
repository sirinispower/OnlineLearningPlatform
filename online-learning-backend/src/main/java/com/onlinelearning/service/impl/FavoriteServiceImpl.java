package com.onlinelearning.service.impl;

import com.onlinelearning.entity.Course;
import com.onlinelearning.entity.Favorite;
import com.onlinelearning.mapper.CourseMapper;
import com.onlinelearning.mapper.FavoriteMapper;
import com.onlinelearning.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<Favorite> getFavoritesByUserId(Long userId) {
        List<Favorite> favorites = favoriteMapper.selectByUserId(userId);
        List<Favorite> result = new ArrayList<>();
        for (Favorite favorite : favorites) {
            if (favorite.getCourseId() == null) {
                favoriteMapper.deleteById(favorite.getId());
                continue;
            }
            Course course = courseMapper.selectCourseById(favorite.getCourseId());
            if (course == null) {
                favoriteMapper.deleteById(favorite.getId());
                continue;
            }
            favorite.setCourse(course);
            result.add(favorite);
        }
        return result;
    }

    @Override
    public void addFavorite(Long userId, Long courseId) {
        if (!isFavorite(userId, courseId)) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setCourseId(courseId);
            favoriteMapper.insert(favorite);
        }
    }

    @Override
    public void removeFavorite(Long userId, Long courseId) {
        Favorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        if (favorite != null) {
            favoriteMapper.deleteById(favorite.getId());
        }
    }

    @Override
    public void removeByCourseId(Long courseId) {
        favoriteMapper.deleteByCourseId(courseId);
    }

    @Override
    public boolean isFavorite(Long userId, Long courseId) {
        return favoriteMapper.selectByUserAndCourse(userId, courseId) != null;
    }
}
