package dev.francisbernas.taskmasterserver.integration.service;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.UserService;
import dev.francisbernas.taskmasterserver.service.impl.UserServiceImpl;
import dev.francisbernas.taskmasterserver.testdata.UserTestData;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class UserServiceImplIT {
	@Autowired
	private UserRepository userRepository;

	private UserService userService;

	private List<User> testUsers;

	@PostConstruct
	public void setUpAll() {
		// initialize userService
		userService = new UserServiceImpl(userRepository);
	}

	@BeforeEach
	public void setUp() {
		// Initialize test data before each test method
		testUsers = UserTestData.getUsersTestData;
		userRepository.saveAll(testUsers);
	}

	@AfterEach
	public void tearDown() {
		// Release test data after each test method
		userRepository.deleteAll();
	}

	@Test
	public void testGetAllUsersExcludingSoftDeleted() {
		Long usersExcludingSoftDeletedCount = testUsers.stream().filter(user -> user.getDeletedBy() == null).count();
		List<UserDto> savedUsers = userService.getAllUsers();
		assertNotNull(savedUsers);
		assertEquals(usersExcludingSoftDeletedCount, savedUsers.size());
	}

	@Test
	public void testGetUserByIdWhereUserIsExisting() {
		UserDto userDto = userService.getUserById(1L);
		assertNotNull(userDto);
	}

	@Test
	public void testGetUserByIdWhereUserIsNotExisting() {
		UserDto userDto = userService.getUserById(100L);
		assertNull(userDto);
	}

	@Test
	public void testGetUserByIdWhereUserIsSoftDeleted() {
		UserDto userDto = userService.getUserById(4L);
		assertNull(userDto);
	}

	@Test
	public void testCreateUser() {
		long savedUsersCount = userRepository.findAll().size();

		UserDto userDto = new UserDto()
				.setUsername("conor123")
				.setFirstName("Conor")
				.setLastName("McGregor")
				.setPassword("xjnuh389h");
		userService.createUser(userDto);

		UserDto createdUserDto = userService.getUserById(savedUsersCount + 1);
		assertNotNull(createdUserDto);
	}

	@Test
	public void testCreateUserWhereDtoHasNonNullId() {
		UserDto userDto = new UserDto()
				.setId(5L)
				.setUsername("conor123")
				.setFirstName("Conor")
				.setLastName("McGregor")
				.setPassword("xjnuh389h");
		assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
	}

	@Test
	public void testSoftDeleteUserByIdWhereUserIsExisting() {
		boolean isDeleted = userService.softDeleteUserById(1L);
		assertTrue(isDeleted);
	}

	@Test
	public void testSoftDeleteUserByIdWhereUserIsNotExisting() {
		boolean isDeleted = userService.softDeleteUserById(100L);
		assertFalse(isDeleted);
	}

	@Test
	public void testUpdateUser() {
		UserDto userDto = new UserDto()
				.setId(1L)
				.setUsername("jdoe")
				.setFirstName("John")
				.setLastName("Doe")
				.setPassword("f823hfas");
		UserDto updatedUserDto = userService.updateUserById(1L, userDto);
		assertEquals(userDto.getUsername(), updatedUserDto.getUsername());
	}

	@Test
	public void testUpdateUserWhereIdArgAndDtoIdMismatch() {
		UserDto userDto = new UserDto()
				.setId(1L)
				.setUsername("jdoe")
				.setFirstName("John")
				.setLastName("Doe")
				.setPassword("f823hfas");
		assertThrows(RuntimeException.class, () -> userService.updateUserById(2L, userDto));
	}
}
