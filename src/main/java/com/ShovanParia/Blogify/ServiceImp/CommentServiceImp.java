
package com.ShovanParia.Blogify.ServiceImp;

import com.ShovanParia.Blogify.Dto.CommentDto;
import com.ShovanParia.Blogify.Entity.Comment;
import com.ShovanParia.Blogify.Entity.Post;
import com.ShovanParia.Blogify.Entity.User;

import com.ShovanParia.Blogify.ExceptionHandling.CommentsNotFound;
import com.ShovanParia.Blogify.ExceptionHandling.PostNotFoundError;
import com.ShovanParia.Blogify.ExceptionHandling.UserNotFoundException;

import com.ShovanParia.Blogify.Repository.CommentRepo;
import com.ShovanParia.Blogify.Repository.PostRepo;
import com.ShovanParia.Blogify.Repository.UserRepo;

import com.ShovanParia.Blogify.Service.CommentService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImp implements CommentService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final ModelMapper modelMapper;

    // CREATE COMMENT
    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new PostNotFoundError("Post not found: " + postId)
                );

        User user = getLoggedInUser();

        validateCommentContent(commentDto);

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPost(post);
        comment.setUser(user);

        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        return convertToDto(commentRepo.save(comment));
    }

    // UPDATE COMMENT
    @Override
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new CommentsNotFound("Comment not found: " + commentId)
                );

        User user = getLoggedInUser();

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new AccessDeniedException(
                    "You cannot update another user's comment"
            );
        }

        validateCommentContent(commentDto);

        comment.setContent(commentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        return convertToDto(commentRepo.save(comment));
    }

    // DELETE COMMENT
    @Override
    public void deleteComment(Long commentId) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new CommentsNotFound("Comment not found: " + commentId)
                );

        User user = getLoggedInUser();

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new AccessDeniedException(
                    "You cannot delete another user's comment"
            );
        }

        commentRepo.delete(comment);
    }

    // GET COMMENT BY ID
    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new CommentsNotFound("Comment not found: " + commentId)
                );

        return convertToDto(comment);
    }

    // GET ALL COMMENTS BY POST ID
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(Long postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new PostNotFoundError("Post not found: " + postId)
                );

        return commentRepo.findByPost(post)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // GET LOGGED-IN USER
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {

            throw new AccessDeniedException("Please login first");
        }

        String email = authentication.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found: " + email)
                );
    }

    // VALIDATE COMMENT
    private void validateCommentContent(CommentDto commentDto) {

        if (commentDto == null ||
                commentDto.getContent() == null ||
                commentDto.getContent().isBlank()) {

            throw new IllegalArgumentException(
                    "Comment content cannot be empty"
            );
        }

        if (commentDto.getContent().length() > 1000) {
            throw new IllegalArgumentException(
                    "Comment cannot exceed 1000 characters"
            );
        }
    }

    // ENTITY TO DTO
    private CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
