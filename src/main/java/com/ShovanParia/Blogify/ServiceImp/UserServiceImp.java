
package com.ShovanParia.Blogify.ServiceImp;

import com.ShovanParia.Blogify.Dto.UserDto;
import com.ShovanParia.Blogify.Entity.User;
import com.ShovanParia.Blogify.ExceptionHandling.UserNotFoundException;
import com.ShovanParia.Blogify.Repository.UserRepo;
import com.ShovanParia.Blogify.Service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

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

        User existingUser = userRepo.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + userId
                ));

        // Update only the fields supplied by the client
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null
                && !userDto.getPassword().isBlank()) {
            existingUser.setPassword(userDto.getPassword());
        }

        if (userDto.getAbout() != null) {
            existingUser.setAbout(userDto.getAbout());
        }

        User updatedUser = userRepo.save(existingUser);

        return convertToDto(updatedUser);
    }

    // Get User By ID
    @Override
    public UserDto getUseById(Long userId) {

        User user = userRepo.findById(Math.toIntExact(userId))
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

        User user = userRepo.findById(Math.toIntExact(userId))
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
