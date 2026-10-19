package com.ShovanParia.Blogify.Repository;

import com.ShovanParia.Blogify.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface UserRepo extends JpaRepository<User,Integer> {


}
