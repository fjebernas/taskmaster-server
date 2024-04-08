package dev.francisbernas.taskmasterserver.unit.service;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.mapper.UserMapper;
import dev.francisbernas.taskmasterserver.repository.ProjectRepository;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.impl.ProjectServiceImpl;
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
public class ProjectServiceImplTest {
	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ProjectServiceImpl projectService;

	private final User testUser = new User(1L, "johnDoe123", "John", "Doe", "f2k0j32f", Collections.emptyList());

	private final UserDto testUserDto = UserMapper.mapEntityToDto(testUser);

	@Test
	public void testGetAllProjects() {
		List<Project> testProjects = List.of(
				new Project(1L, "good project", "a project that does good things", testUser),
				new Project(2L, "bad project", "a project that does bad things", testUser)
		);

		Mockito.when(projectRepository.findAllExcludingSoftDeleted()).thenReturn(testProjects);

		Assertions.assertEquals(
				testProjects.size(),
				projectService.getAllProjects().size()
		);
	}

	@Test
	public void testGetAllProjectsExcludingSoftDeleted() {
		List<Project> testProjects = List.of(
				new Project(1L, "good project", "a project that does good things", testUser),
				new Project(2L, "bad project", "a project that does bad things", testUser)
		);

		// Soft delete second project
		Project softDeletedProject = testProjects.get(1);
		softDeletedProject.setDeletedBy("admin");
		softDeletedProject.setDeletedDate(LocalDateTime.now());

		// This imitates the query used by the service class from the repository interface
		// See method findAllNotDeleted()
		Mockito.when(projectRepository.findAllExcludingSoftDeleted())
				.thenReturn(
						testProjects
								.stream()
								.filter(project -> project.getDeletedBy() == null)
								.toList()
				);

		Assertions.assertEquals(
				1,
				projectService.getAllProjects().size()
		);
	}

	@Test
	public void testGetAllProjectsByUserId() {
		List<Project> testProjects = List.of(
				new Project(1L, "good project", "a project that does good things", testUser),
				new Project(2L, "bad project", "a project that does bad things", testUser)
		);

		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(testUser));
		Mockito.when(projectRepository.findAllByUserExcludingSoftDeleted(testUser)).thenReturn(testProjects);

		Assertions.assertEquals(
				testProjects.size(),
				projectService.getAllProjectsByUserId(1L).size()
		);
	}

	@Test
	public void testGetProjectByIdWhereProjectIsExisting() {
		Project testProject = new Project(1L, "good project", "a project that does good things", testUser);
		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(testProject));

		ProjectDto projectDto = projectService.getProjectById(1L);
		Assertions.assertNotNull(projectDto);
		Assertions.assertEquals(testProject.getId(), projectDto.getId());
	}

	@Test
	public void testGetProjectByIdWhereProjectIsNotExisting() {
		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.empty());

		Assertions.assertNull(projectService.getProjectById(1L));
	}

	@Test
	public void testGetProjectByIdWhereProjectIsNotSoftDeleted() {
		Project testProject = new Project(1L, "good project", "a project that does good things", testUser);

		// Soft delete the project
		testProject.setDeletedBy("admin");
		testProject.setDeletedDate(LocalDateTime.now());

		// This imitates the query used by the service class from the repository interface
		// See method findByIdNotDeleted()
		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L))
				.thenReturn(testProject.getDeletedBy() == null ? Optional.of(testProject) : Optional.empty());

		Assertions.assertNull(
				projectService.getProjectById(1L)
		);
	}

	@Test
	public void testCreateProject() {
		ProjectDto projectDto = new ProjectDto(null, "good project", "a project that does good things", testUserDto);

		Project projectEntity = new Project(1L, "good project", "a project that does good things", testUser);
		Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(projectEntity);

		Mockito.when(userRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(testUser));

		ProjectDto result = projectService.createProject(projectDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(projectEntity.getName(), result.getName());
	}

	@Test
	public void testCreateProjectWhereDtoHasNonNullId() {
		ProjectDto projectDto = new ProjectDto(1L, "good project", "a project that does good things", testUserDto);

		Assertions.assertThrows(RuntimeException.class, () -> projectService.createProject(projectDto));
	}

	@Test
	public void testSoftDeleteUserByIdWhereUserIsExisting() {
		Project testProject = new Project(1L, "good project", "a project that does good things", testUser);
		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(testProject));

		boolean result = projectService.softDeleteProjectById(1L);

		Assertions.assertTrue(result);
	}

	@Test
	public void testSoftDeleteUserByIdWhereUserIsNotExisting() {
		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.empty());

		boolean result = projectService.softDeleteProjectById(1L);

		Assertions.assertFalse(result);
	}

	@Test
	public void testUpdateProjectById() {
		Project existingProject = new Project(1L, "good project", "a project that does good things", testUser);
		Project updatedProject = new Project(1L, "very good project", "a project that does good things", testUser);

		Mockito.when(projectRepository.findByIdNotSoftDeleted(1L)).thenReturn(Optional.of(existingProject));
		Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(updatedProject);

		ProjectDto requestProjectDto = new ProjectDto(1L, "very good project", "a project that does good things", testUserDto);
		ProjectDto result = projectService.updateProjectById(1L, requestProjectDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(updatedProject.getName(), result.getName());
	}
}
