package com.ShovanParia.Blogify.Service;

import com.ShovanParia.Blogify.Dto.UserDto;

import java.util.List;

public interface UserService {
  UserDto registerNewUser(UserDto userDto);
  UserDto createUser(UserDto userDto);
  UserDto updateUser(UserDto userDto,Long userId);
  UserDto getUseById(Long userId);
  List<UserDto> getAllUsers();
  Void deleteUser(Long userId);

}
