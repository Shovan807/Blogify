
package com.ShovanParia.Blogify.ServiceImp;

import com.ShovanParia.Blogify.Dto.UserDto;
import com.ShovanParia.Blogify.Entity.Role;
import com.ShovanParia.Blogify.Entity.User;
import com.ShovanParia.Blogify.ExceptionHandling.UserNotFoundException;
import com.ShovanParia.Blogify.Repository.RoleRepo;
import com.ShovanParia.Blogify.Repository.UserRepo;
import com.ShovanParia.Blogify.Service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public UserDto registerNewUser(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        // 1. Encode password
        user.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        // 2. Fetch default role
        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("Default role not found"));

        // 3. Assign role to user
        user.setRoleSet(new HashSet<>(Set.of(role)));

        // 4. Save user with assigned role
        User savedUser = userRepo.save(user);

        // 5. Convert entity to DTO
        UserDto response = modelMapper.map(savedUser, UserDto.class);

        // 6. Hide password
        response.setPassword(null);

        return response;
    }

    // Create User
    @Override
    public UserDto createUser(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        // Allow the database to generate the ID
        user.setId(null);
        User savedUser = userRepo.save(user);

        return convertToDto(savedUser);
    }


    // Update User
    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {

        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + userId
                ));

        // Update only the fields supplied by the client
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
            System.out.println(userDto.getEmail());
        }

        if (userDto.getPassword() != null
                && !userDto.getPassword().isBlank()) {
            existingUser.setPassword(userDto.getPassword());
        }

        if (userDto.getAbout() != null) {
            existingUser.setAbout(userDto.getAbout());
            System.out.println("About received: " + userDto.getAbout());
        }


        User updatedUser = userRepo.save(existingUser);

        return convertToDto(updatedUser);
    }

    // Get User By ID
    @Override
    public UserDto getUseById(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + userId
                ));

        return convertToDto(user);
    }

    // Get All Users
    @Override
    public List<UserDto> getAllUsers() {

        return userRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Delete User
    @Override
    public Void deleteUser(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + userId
                ));

        userRepo.delete(user);

        return null;
    }

    // Convert Entity to DTO
    private UserDto convertToDto(User user) {

        UserDto userDto = modelMapper.map(user, UserDto.class);

        // Never return the user's password
        userDto.setPassword(null);

        return userDto;
    }

}
