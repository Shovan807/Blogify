package com.ShovanParia.Blogify.Config;

import com.ShovanParia.Blogify.Entity.Role;
import com.ShovanParia.Blogify.Repository.RoleRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepo roleRepo) {

        return args -> {

            if (roleRepo.findByName("ROLE_USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepo.save(userRole);
            }

            if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepo.save(adminRole);
            }
        };
    }
}