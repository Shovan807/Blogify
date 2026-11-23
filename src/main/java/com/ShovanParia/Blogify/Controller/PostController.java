package com.ShovanParia.Blogify.Controller;

import com.ShovanParia.Blogify.Dto.PostDto;
import com.ShovanParia.Blogify.Entity.Post;
import com.ShovanParia.Blogify.Service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
        //lec 21 watching ...
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Create post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        PostDto createdPost =
                postService.createPost(postDto, userId, categoryId);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // Update post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(
             @RequestBody PostDto postDto,
            @PathVariable Long postId) {

        PostDto updatedPost = postService.updatePost(postDto, postId);

        return ResponseEntity.ok(updatedPost);
    }

    // Get posts by user ID
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getByUserId(
            @PathVariable Long userId) {

        List<PostDto> posts = postService.getPostsByUser(userId);

        return ResponseEntity.ok(posts);
    }

    // Get posts by category ID
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getByCategoryId(
            @PathVariable Long categoryId) {

        List<PostDto> posts = postService.getByCategory(categoryId);

        return ResponseEntity.ok(posts);
    }

    // Get all posts
    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize) {

        List<PostDto> posts =
                postService.getAllPost(pageNumber, pageSize);

        return ResponseEntity.ok(posts);
    }


    // Delete post
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }
}