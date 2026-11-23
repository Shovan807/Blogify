package com.ShovanParia.Blogify.Service;

import com.ShovanParia.Blogify.Dto.CommentDto;

import java.util.List;

public interface CommentService {

    // Create a comment for a particular post
    CommentDto createComment(Long postId, CommentDto commentDto);

    // Update an existing comment
    CommentDto updateComment(Long commentId, CommentDto commentDto);

    // Delete a comment
    void deleteComment(Long commentId);

    // Get a particular comment
    CommentDto getCommentById(Long commentId);

    // Get all comments of a particular post
    List<CommentDto> getCommentsByPostId(Long postId);


}