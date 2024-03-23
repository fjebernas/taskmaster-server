package dev.francisbernas.taskmasterserver.controller;

import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("${serverBaseUrl}/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
