package com.onlinelearning.service.impl;

import com.onlinelearning.entity.Favorite;
import com.onlinelearning.mapper.FavoriteMapper;
import com.onlinelearning.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteMapper.selectByUserId(userId);
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
    public boolean isFavorite(Long userId, Long courseId) {
        return favoriteMapper.selectByUserAndCourse(userId, courseId) != null;
    }
}
