package dev.francisbernas.taskmasterserver.service;

import dev.francisbernas.taskmasterserver.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto userDto);

    boolean deleteUserById(Long userId);

    UserDto updateUserById(Long userId, UserDto userDto);
}
