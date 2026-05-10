package com.onlinelearning.service;

import com.onlinelearning.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByCourseId(Long courseId);

    void saveComment(Comment comment);

    void deleteComment(Long id);

    Double getAverageRating(Long courseId);
}
