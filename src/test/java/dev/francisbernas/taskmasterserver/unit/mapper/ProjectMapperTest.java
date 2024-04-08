package dev.francisbernas.taskmasterserver.unit.mapper;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.mapper.ProjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectMapperTest {

	@Test
	public void testMapEntityToDto() {
		// Given
		User userEntity = new User()
				.setId(1L)
				.setUsername("johnDoe123")
				.setFirstName("John")
				.setLastName("Doe")
				.setPassword("hv7u8hwe789");
		Project projectEntity = new Project(1L, "good project", "a project that does good things", userEntity);

		// When
		ProjectDto projectDto = ProjectMapper.mapEntityToDto(projectEntity);

		// Then
		assertEquals(projectEntity.getId(), projectDto.getId());
		assertEquals(projectEntity.getName(), projectDto.getName());
		assertEquals(projectEntity.getDescription(), projectDto.getDescription());
		assertEquals(projectEntity.getUser().getId(), projectDto.getUserDto().getId());
		assertEquals(projectEntity.getUser().getUsername(), projectDto.getUserDto().getUsername());
		assertEquals(projectEntity.getUser().getFirstName(), projectDto.getUserDto().getFirstName());
		assertEquals(projectEntity.getUser().getLastName(), projectDto.getUserDto().getLastName());
		assertEquals(projectEntity.getUser().getPassword(), projectDto.getUserDto().getPassword());
	}

	@Test
	public void testMapEntitiesToDtos() {
		// Given
		User userEntity = new User()
				.setId(1L)
				.setUsername("johnDoe123")
				.setFirstName("John")
				.setLastName("Doe")
				.setPassword("hv7u8hwe789");
		List<Project> projectEntities = Arrays.asList(
				new Project(1L, "good project", "a project that does good things", userEntity),
				new Project(2L, "bad project", "a project that does bad things", userEntity)
		);

		// When
		List<ProjectDto> projectDtos = ProjectMapper.mapEntitiesToDtos(projectEntities);

		// Then
		assertEquals(projectEntities.size(), projectDtos.size());
		for (int i = 0; i < projectEntities.size(); i++) {
			assertEquals(projectEntities.get(i).getId(), projectDtos.get(i).getId());
			assertEquals(projectEntities.get(i).getName(), projectDtos.get(i).getName());
			assertEquals(projectEntities.get(i).getDescription(), projectDtos.get(i).getDescription());
			assertEquals(projectEntities.get(i).getUser().getId(), projectDtos.get(i).getUserDto().getId());
			assertEquals(projectEntities.get(i).getUser().getUsername(), projectDtos.get(i).getUserDto().getUsername());
			assertEquals(projectEntities.get(i).getUser().getFirstName(), projectDtos.get(i).getUserDto().getFirstName());
			assertEquals(projectEntities.get(i).getUser().getLastName(), projectDtos.get(i).getUserDto().getLastName());
			assertEquals(projectEntities.get(i).getUser().getPassword(), projectDtos.get(i).getUserDto().getPassword());
		}
	}

	@Test
	public void testMapDtoToEntity() {
		// Given
		ProjectDto projectDto = new ProjectDto()
				.setId(1L)
				.setName("good project")
				.setDescription("a project that does good things")
				.setUserDto(
						new UserDto()
								.setId(1L)
								.setUsername("johnDoe123")
								.setFirstName("John")
								.setLastName("Doe")
								.setPassword("jf2fn298h2")
				);

		// When
		Project projectEntity = ProjectMapper.mapDtoToEntity(projectDto);

		// Then
		assertEquals(projectDto.getId(), projectEntity.getId());
		assertEquals(projectDto.getName(), projectEntity.getName());
		assertEquals(projectDto.getDescription(), projectEntity.getDescription());
		assertEquals(projectDto.getUserDto().getId(), projectEntity.getUser().getId());
		assertEquals(projectDto.getUserDto().getUsername(), projectEntity.getUser().getUsername());
		assertEquals(projectDto.getUserDto().getFirstName(), projectEntity.getUser().getFirstName());
		assertEquals(projectDto.getUserDto().getLastName(), projectEntity.getUser().getLastName());
		assertEquals(projectDto.getUserDto().getPassword(), projectEntity.getUser().getPassword());
	}
}
