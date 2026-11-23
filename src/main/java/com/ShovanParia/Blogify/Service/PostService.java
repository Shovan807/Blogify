package com.ShovanParia.Blogify.Service;

import com.ShovanParia.Blogify.Dto.PostDto;
import com.ShovanParia.Blogify.Entity.Post;

import java.util.List;

public interface PostService {
    //create
    PostDto createPost(PostDto postDto, Long uId, Long categoryId);


    //update
    PostDto updatePost(PostDto postDto, Long postId);

    //delete
    void deletePost(Long postId);

    //getAllpost
    List<PostDto> getAllPost(Integer pageNumber, Integer pageSize);

    //get  post by Id
    PostDto getPostById(Long postId);

    //get posts By Catagory
    List<PostDto> getByCategory(Long categoryId);

    //get all Posts by user
    List<PostDto> getPostsByUser(Long userId);

    //search Posts
    List<PostDto> searchPosts(String keyword);


}
