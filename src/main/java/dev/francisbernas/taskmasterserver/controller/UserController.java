package dev.francisbernas.taskmasterserver.controller;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("${serverBaseUrl}/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Retrieving all users");
        List<UserDto> userDtos = userService.getAllUsers();
        log.info("Retrieved {} users", userDtos.size());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Attempting to retrieve user with id {}", userId);
        UserDto userDto = userService.getUserById(userId);
        if (userDto != null) {
            log.info("Successfully retrieved user with id {}", userId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            log.info("Failed to retrieve user with id {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Creating user: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        log.info("User created: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        log.info("Attempting to delete user with id {}", userId);
        boolean isDeleted = userService.softDeleteUserById(userId);
        if (isDeleted) {
            log.info("User with id {} deleted successfully", userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("User with id {} not found, deletion failed", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Updating user with id {}: {}", userId, userDto);
        UserDto updatedUserDto = userService.updateUserById(userId, userDto);
        log.info("User with id {} updated successfully: {}", userId, updatedUserDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

}
