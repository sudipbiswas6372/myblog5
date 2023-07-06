package com.blog.service;

import com.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
   // List<CommentDto> getCommentsByPostId = ;

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(long postId, long commentId);

    CommentDto updateComment(long commentId, long postId, CommentDto commentDto);

    void deleteComment(long postId, long commentId);
}
