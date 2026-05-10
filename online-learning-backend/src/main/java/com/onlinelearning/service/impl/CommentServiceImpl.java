package com.onlinelearning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onlinelearning.entity.Comment;
import com.onlinelearning.mapper.CommentMapper;
import com.onlinelearning.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByCourseId(Long courseId) {
        return commentMapper.selectByCourseId(courseId);
    }

    @Override
    public void saveComment(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentMapper.deleteById(id);
    }

    @Override
    public Double getAverageRating(Long courseId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCourseId, courseId);
        wrapper.isNotNull(Comment::getRating);
        List<Comment> comments = commentMapper.selectList(wrapper);

        if (comments.isEmpty()) {
            return 0.0;
        }

        double sum = comments.stream().mapToInt(Comment::getRating).sum();
        return sum / comments.size();
    }
}
