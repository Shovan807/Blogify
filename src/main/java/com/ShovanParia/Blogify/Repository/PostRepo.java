package com.ShovanParia.Blogify.Repository;

import com.ShovanParia.Blogify.Entity.Category;
import com.ShovanParia.Blogify.Entity.Post;
import com.ShovanParia.Blogify.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Long> {

    // Jpa provides all method but specific some method not provide so i need to create it

    List<Post> findByUser(User user);
   List<Post> findByCategory(Category category);



        List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                String title,
                String content
        );

}
