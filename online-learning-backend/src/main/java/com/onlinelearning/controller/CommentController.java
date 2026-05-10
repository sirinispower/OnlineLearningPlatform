package com.onlinelearning.controller;

import com.onlinelearning.dto.Result;
import com.onlinelearning.entity.Comment;
import com.onlinelearning.service.CommentService;
import com.onlinelearning.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/course/{courseId}")
    public Result<List<Comment>> getByCourseId(@PathVariable Long courseId) {
        return Result.success(commentService.getCommentsByCourseId(courseId));
    }

    @PostMapping
    public Result<?> save(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        comment.setUserId(userId);
        commentService.saveComment(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }

    @GetMapping("/rating/{courseId}")
    public Result<Double> getAverageRating(@PathVariable Long courseId) {
        return Result.success(commentService.getAverageRating(courseId));
    }
}
