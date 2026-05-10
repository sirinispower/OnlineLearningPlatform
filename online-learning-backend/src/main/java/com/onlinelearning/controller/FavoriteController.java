package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.Favorite;
import com.onlinelearning.service.FavoriteService;
import com.onlinelearning.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Result<List<Favorite>> getFavorites(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(favoriteService.getFavoritesByUserId(userId));
    }

    @PostMapping("/{courseId}")
    public Result<?> addFavorite(@PathVariable Long courseId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        favoriteService.addFavorite(userId, courseId);
        return Result.success();
    }

    @DeleteMapping("/{courseId}")
    public Result<?> removeFavorite(@PathVariable Long courseId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        favoriteService.removeFavorite(userId, courseId);
        return Result.success();
    }

    @GetMapping("/check/{courseId}")
    public Result<Boolean> isFavorite(@PathVariable Long courseId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(favoriteService.isFavorite(userId, courseId));
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
