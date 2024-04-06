package dev.francisbernas.taskmasterserver.service;


import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	public void testGetAllUsers() {
		List<User> users = List.of(
				new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList()),
				new User(2L, "maryGrace123", "Mary", "Grace", "h283gf2", Collections.emptyList())
		);

		Mockito.when(userRepository.findAllNotDeleted()).thenReturn(users);

		Assertions.assertEquals(
				users.size(),
				userService.getAllUsers().size()
		);
	}

	@Test
	public void testGetUserByIdWhereUserIsExisting() {
		List<User> users = List.of(
				new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList()),
				new User(2L, "maryGrace123", "Mary", "Grace", "h283gf2", Collections.emptyList())
		);

		Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.of(users.get(0)));

		Assertions.assertEquals(
				users.get(0).getId(),
				userService.getUserById(1L).getId()
		);

		Mockito.when(userRepository.findByIdNotDeleted(2L)).thenReturn(Optional.of(users.get(1)));

		Assertions.assertEquals(
				users.get(1).getId(),
				userService.getUserById(2L).getId()
		);
	}

	@Test
	public void testGetUserByIdWhereUserIsNotExisting() {
		Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.empty());

		Assertions.assertNull(userService.getUserById(1L));
	}

	@Test
	public void testCreateUser() {
		UserDto userDto = new UserDto(null, "johnDoe123", "John", "Doe", "f2k0j32f");

		User userEntity = new User(null, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userEntity);

		UserDto result = userService.createUser(userDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(userEntity.getUsername(), result.getUsername());
	}

	@Test
	public void testDeleteUserByIdWhereUserIsExisting() {
		User user = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());
		Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.of(user));

		boolean result = userService.softDeleteUserById(1L);

		Assertions.assertTrue(result);
	}

	@Test
	public void testDeleteUserByIdWhereUserIsNotExisting() {
		Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.empty());

		boolean result = userService.softDeleteUserById(1L);

		Assertions.assertFalse(result);
	}

	@Test
	public void testUpdateUserById() {
		User existingUser = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());
		User updatedUser = new User(1L, "spectacularDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());

		Mockito.when(userRepository.findByIdNotDeleted(1L)).thenReturn(Optional.of(existingUser));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

		UserDto updateRequestuserDto = new UserDto(1L, "spectacularDoe123", "John", "Doe", "f2k0j32f");
		UserDto result = userService.updateUserById(1L, updateRequestuserDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(updatedUser.getUsername(), result.getUsername());
	}
}