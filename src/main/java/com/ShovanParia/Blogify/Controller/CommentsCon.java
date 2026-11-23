package com.ShovanParia.Blogify.Controller;

import com.ShovanParia.Blogify.Dto.CommentDto;
import com.ShovanParia.Blogify.Service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api")
@RestController
public class CommentsCon {


    private final CommentService commentService;

    public CommentsCon(CommentService commentService) {
        this.commentService = commentService;
    }


    // 1. CREATE COMMENT ON A PARTICULAR POST


    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDto commentDto
    ) {
        System.out.println(commentDto.getContent());
        CommentDto createdComment =
                commentService.createComment(postId, commentDto);

        return new ResponseEntity<>(
                createdComment,
                HttpStatus.CREATED
        );
    }



    // 2. UPDATE COMMENT


    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto
    ) {

        CommentDto updatedComment =
                commentService.updateComment(commentId, commentDto);

        return ResponseEntity.ok(updatedComment);
    }



    // 3. DELETE COMMENT


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId
    ) {

        commentService.deleteComment(commentId);

        return ResponseEntity.ok("Comment deleted successfully");
    }



    // 4. GET COMMENT BY COMMENT ID


    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long commentId
    ) {

        CommentDto comment =
                commentService.getCommentById(commentId);

        return ResponseEntity.ok(comment);
    }



    // 5. GET ALL COMMENTS BY POST ID


    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(
            @PathVariable Long postId
    ) {

        List<CommentDto> comments =
                commentService.getCommentsByPostId(postId);

        return ResponseEntity.ok(comments);
    }



}
