package dev.francisbernas.taskmasterserver.service;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        // Mock repository to return a list of users
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "johnDoe123", "John", "Doe", "password", Collections.emptyList()));
        Mockito.when(userRepository.findAllNotDeleted()).thenReturn(userList);

        List<UserDto> userDtoList = userService.getAllUsers();
        Assertions.assertEquals(1, userDtoList.size());
        Assertions.assertEquals("John", userDtoList.get(0).getFirstName());
    }

    @Test
    public void testGetUserById() {
        // Mock repository to return a user
        User user = new User(1L, "johnDoe123", "John", "Doe", "password", Collections.emptyList());
        Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(1L);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals("John", userDto.getFirstName());
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto(null, "janeSmith345", "Jane", "Smith", "password");
        User userEntity = new User(null, "janeSmith345", "Jane", "Smith", "password", Collections.emptyList());

        // Mock repository save method
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userEntity);

        UserDto createdUserDto = userService.createUser(userDto);
        Assertions.assertNotNull(createdUserDto);
        Assertions.assertEquals("Jane", createdUserDto.getFirstName());
    }

    @Test
    public void testDeleteUserById() {
        User user = new User(1L, "johnDoe123", "John", "Doe", "password", Collections.emptyList());

        // Mock repository findByIdNotDeleted method
        Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.of(user));

        boolean isDeleted = userService.deleteUserById(1L);
        Assertions.assertTrue(isDeleted);
    }

//    @Test
//    public void testUpdateUserById() {
//    }

}
