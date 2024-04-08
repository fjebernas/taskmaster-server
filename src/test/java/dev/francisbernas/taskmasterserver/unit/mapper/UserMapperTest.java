package dev.francisbernas.taskmasterserver.unit.mapper;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

	@Test
	public void testMapEntityToDto() {
		// Given
		User userEntity = new User()
				.setId(1L)
				.setUsername("johnDoe123")
				.setFirstName("John")
				.setLastName("Doe")
				.setPassword("hv7u8hwe789");

		// When
		UserDto userDto = UserMapper.mapEntityToDto(userEntity);

		// Then
		assertEquals(userEntity.getId(), userDto.getId());
		assertEquals(userEntity.getUsername(), userDto.getUsername());
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getPassword(), userDto.getPassword());
	}

	@Test
	public void testMapEntitiesToDtos() {
		// Given
		List<User> userEntities = Arrays.asList(
				new User()
						.setId(1L)
						.setUsername("johnDoe123")
						.setFirstName("John")
						.setLastName("Doe")
						.setPassword("hv7u8hwe789"),
				new User()
						.setId(2L)
						.setUsername("mGrace123")
						.setFirstName("Mary")
						.setLastName("Grace")
						.setPassword("nvmi93uj12sa")
		);

		// When
		List<UserDto> userDtos = UserMapper.mapEntitiesToDtos(userEntities);

		// Then
		assertEquals(userEntities.size(), userDtos.size());
		for (int i = 0; i < userEntities.size(); i++) {
			assertEquals(userEntities.get(i).getId(), userDtos.get(i).getId());
			assertEquals(userEntities.get(i).getUsername(), userDtos.get(i).getUsername());
			assertEquals(userEntities.get(i).getFirstName(), userDtos.get(i).getFirstName());
			assertEquals(userEntities.get(i).getLastName(), userDtos.get(i).getLastName());
			assertEquals(userEntities.get(i).getPassword(), userDtos.get(i).getPassword());
		}
	}

	@Test
	public void testMapDtoToEntity() {
		// Given
		UserDto userDto = new UserDto(1L, "johnDoe123", "John", "Doe", "f89h2jsadas");

		// When
		User userEntity = UserMapper.mapDtoToEntity(userDto);

		// Then
		assertEquals(userDto.getId(), userEntity.getId());
		assertEquals(userDto.getUsername(), userEntity.getUsername());
		assertEquals(userDto.getFirstName(), userEntity.getFirstName());
		assertEquals(userDto.getLastName(), userEntity.getLastName());
		assertEquals(userDto.getPassword(), userEntity.getPassword());
	}
}
