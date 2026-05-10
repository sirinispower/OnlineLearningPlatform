package com.onlinelearning.service;

import com.onlinelearning.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getFavoritesByUserId(Long userId);

    void addFavorite(Long userId, Long courseId);

    void removeFavorite(Long userId, Long courseId);

    boolean isFavorite(Long userId, Long courseId);
}
