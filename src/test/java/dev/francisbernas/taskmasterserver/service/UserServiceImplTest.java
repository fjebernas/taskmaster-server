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

import java.time.LocalDateTime;
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

		Mockito.when(userRepository.findAllExcludingSoftDeleted()).thenReturn(users);

		Assertions.assertEquals(
				users.size(),
				userService.getAllUsers().size()
		);
	}

	@Test
	public void testGetAllUsersExcludingSoftDeleted() {
		List<User> users = List.of(
				new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList()),
				new User(2L, "maryGrace123", "Mary", "Grace", "h283gf2", Collections.emptyList()),
				new User(2L, "sabReyes123", "Sabrina", "Reyes", "zncxv92", Collections.emptyList())
		);

		// Soft delete the third user
		User softDeletedUser = users.get(2);
		softDeletedUser.setDeletedBy("admin");
		softDeletedUser.setDeletedDate(LocalDateTime.now());

		// This imitates the query used by the service class from the repository interface
		// See method findAllNotDeleted()
		Mockito.when(userRepository.findAllExcludingSoftDeleted())
				.thenReturn(
						users
						.stream()
						.filter(user -> user.getDeletedBy() == null)
						.toList()
				);

		Assertions.assertEquals(
				2,
				userService.getAllUsers().size()
		);
	}

	@Test
	public void testGetUserByIdWhereUserIsExisting() {
		List<User> users = List.of(
				new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList()),
				new User(2L, "maryGrace123", "Mary", "Grace", "h283gf2", Collections.emptyList())
		);

		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(users.get(0)));

		Assertions.assertEquals(
				users.get(0).getId(),
				userService.getUserById(1L).getId()
		);

		Mockito.when(userRepository.findByIdNotSoftDeleted(2L)).thenReturn(Optional.of(users.get(1)));

		Assertions.assertEquals(
				users.get(1).getId(),
				userService.getUserById(2L).getId()
		);
	}

	@Test
	public void testGetUserByIdWhereUserIsNotExisting() {
		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.empty());

		Assertions.assertNull(userService.getUserById(1L));
	}

	@Test
	public void testGetUserByIdWhereUserIsNotSoftDeleted() {
		User user = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());

		// Soft delete the user
		user.setDeletedBy("admin");
		user.setDeletedDate(LocalDateTime.now());

		// This imitates the query used by the service class from the repository interface
		// See method findByIdNotDeleted()
		Mockito.when(userRepository.findByIdNotSoftDeleted(1L))
				.thenReturn(user.getDeletedBy() == null ? Optional.of(user) : Optional.empty());

		Assertions.assertNull(
				userService.getUserById(1L)
		);
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
	public void testCreateUserWhereDtoHasNonNullId() {
		UserDto userDto = new UserDto(1L, "johnDoe123", "John", "Doe", "f2k0j32f");

		Assertions.assertThrowsExactly(RuntimeException.class, () -> userService.createUser(userDto));
	}

	@Test
	public void testDeleteUserByIdWhereUserIsExisting() {
		User user = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());
		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(user));

		boolean result = userService.softDeleteUserById(1L);

		Assertions.assertTrue(result);
	}

	@Test
	public void testDeleteUserByIdWhereUserIsNotExisting() {
		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.empty());

		boolean result = userService.softDeleteUserById(1L);

		Assertions.assertFalse(result);
	}

	@Test
	public void testUpdateUserById() {
		User existingUser = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());
		User updatedUser = new User(1L, "spectacularDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());

		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(existingUser));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

		UserDto updateRequestuserDto = new UserDto(1L, "spectacularDoe123", "John", "Doe", "f2k0j32f");
		UserDto result = userService.updateUserById(1L, updateRequestuserDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(updatedUser.getUsername(), result.getUsername());
	}
}
