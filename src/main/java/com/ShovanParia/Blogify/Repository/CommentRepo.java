package com.ShovanParia.Blogify.Repository;

import com.ShovanParia.Blogify.Entity.Comment;
import com.ShovanParia.Blogify.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment,Long> {


    List<Comment> findByPost(Post existingPost);
}
