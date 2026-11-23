package com.ShovanParia.Blogify.ServiceImp;

import com.ShovanParia.Blogify.Dto.PostDto;
import com.ShovanParia.Blogify.Entity.Category;
import com.ShovanParia.Blogify.Entity.Post;
import com.ShovanParia.Blogify.Entity.User;
import com.ShovanParia.Blogify.ExceptionHandling.CategoryNotFoundException;
import com.ShovanParia.Blogify.ExceptionHandling.PostNotFoundError;
import com.ShovanParia.Blogify.ExceptionHandling.UserNotFoundException;
import com.ShovanParia.Blogify.Repository.CategotoryRepo;
import com.ShovanParia.Blogify.Repository.PostRepo;
import com.ShovanParia.Blogify.Repository.UserRepo;
import com.ShovanParia.Blogify.Service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImp  implements PostService {
    @Autowired
    public PostRepo postRepo;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public CategotoryRepo categotoryRepo;
    @Override
    public PostDto createPost(PostDto postDto, Long Uid, Long categoryId) {


        validateUserOwnership(Uid);

        // Check whether the user exists
        User userExist = userRepo.findById(Uid)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + Uid
                ));

        // Check whether the category exists
        Category categoryExist = categotoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + categoryId
                ));

        // Convert DTO to Post entity
        Post post = modelMapper.map(postDto, Post.class);

        // Set additional properties
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(userExist);
        post.setCategory(categoryExist);

        // Save the post
        Post savedData = postRepo.save(post);

        // Convert saved entity to DTO and return
        return modelMapper.map(savedData, PostDto.class);
    }



    private void validateUserOwnership(Long requestedUserId) {

        User loggedInUser = getAuthenticatedUser();

        if (!loggedInUser.getId().equals(requestedUserId)) {
            throw new AccessDeniedException(
                    "You cannot perform this operation for another user"
            );
        }
    }



    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Authenticated user not found"));
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId) {

        // Check whether the post exists
        Post existingPost = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundError(
                        "Post not found with ID: " + postId
                ));

        // Update the existing post

        if(postDto.getCategory()!=null){
            Category category = categotoryRepo.findById(
                    postDto.getCategory().getId()
            ).orElseThrow(() -> new CategoryNotFoundException(
                    "Category not found"
            ));

       existingPost.setCategory(category);

        }

        if(postDto.getContent()!=null){
            existingPost.setContent(postDto.getContent());
        }
        if(postDto.getPostTitle()!=null){
            existingPost.setTitle(postDto.getPostTitle());
        }

        existingPost.setAddedDate(new Date());

        // Save the updated post
        Post newPost=postRepo.save(existingPost);

        //post->postDto
        return this.modelMapper.map(newPost,PostDto.class);


    }

    @Override
    public void deletePost(Long postId) {
        Post existingPost = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundError(
                        "Post not found with ID: " + postId
                ));
        //now we can delete
        postRepo.deleteById(postId);

    }

    @Override
    public List<PostDto> getAllPost(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Post> allPost = postRepo.findAll(pageable);

        return allPost.map(post ->
                modelMapper.map(post, PostDto.class)
        ).getContent();
    }

    @Override
    public PostDto getPostById(Long postId) {

        Post existingPost = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundError(
                        "Post not found with ID: " + postId
                ));

        return modelMapper.map(existingPost,PostDto.class);
    }

    @Override
    public List<PostDto> getByCategory(Long categoryId) {
        //check category exit or not if exit take category
        Category categoryExist = categotoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + categoryId
                ));

        // get post
        // Get all posts belonging to this category
        List<Post> posts = postRepo.findByCategory(categoryExist);

        // Convert List<Post> to List<PostDto>
        List<PostDto> postDtos = posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        return postDtos;
    }

    @Override
    public List<PostDto> getPostsByUser(Long userId) {



        //check category exit or not
        User userExit = userRepo.findById(userId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + userId
                ));

        // get post
        // Get all posts belonging to this category
        
        List<Post> posts = postRepo.findByUser(userExit);

        // Convert List<Post> to List<PostDto>
        List<PostDto> postDtos = posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        // Search posts by title or content
        List<Post> posts =
                postRepo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                        keyword, keyword
                );

        // Convert List<Post> to List<PostDto>
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }
}
