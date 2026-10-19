package com.ShovanParia.Blogify;

import com.ShovanParia.Blogify.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogifyApplicationTests {
@Autowired
private UserRepo userRepo;
	@Test
	void contextLoads() {
	}

	@Test
	public void test(){
		System.out.println(userRepo.getClass().getName());
		System.out.println(userRepo.getClass());
	}

}
