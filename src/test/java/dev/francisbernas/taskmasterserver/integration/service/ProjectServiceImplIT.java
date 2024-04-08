package dev.francisbernas.taskmasterserver.integration.service;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.mapper.UserMapper;
import dev.francisbernas.taskmasterserver.repository.ProjectRepository;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.ProjectService;
import dev.francisbernas.taskmasterserver.service.impl.ProjectServiceImpl;
import dev.francisbernas.taskmasterserver.testdata.ProjectTestData;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class ProjectServiceImplIT {
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	private ProjectService projectService;

	private List<Project> testProjects;

	@PostConstruct
	public void setUpAll() {
		// initialize projectService
		projectService = new ProjectServiceImpl(projectRepository, userRepository);
	}

	@BeforeEach
	public void setUp() {
		// Initialize test data before each test method
		// Save users
		userRepository.save(ProjectTestData.testUser1);
		userRepository.save(ProjectTestData.testUser2);
		// Save projects
		testProjects = ProjectTestData.getProjectsTestData;
		projectRepository.saveAll(testProjects);
	}

	@AfterEach
	public void tearDown() {
		// Release test data after each test method
		projectRepository.deleteAll();
	}

	@Test
	public void testGetAllProjectsExcludingSoftDeleted() {
		Long projectsExcludingSoftDeletedCount = testProjects.stream().filter(project -> project.getDeletedBy() == null).count();
		List<ProjectDto> savedProjects = projectService.getAllProjects();

		assertNotNull(savedProjects);
		assertEquals(projectsExcludingSoftDeletedCount, savedProjects.size());
	}

	@Test
	public void testGetAllProjectsByUserId() {
		Long testUserId = 1L;

		Long user1ProjectsExcludingSoftDeletedCount = testProjects
				.stream()
				.filter(project -> Objects.equals(project.getUser().getId(), testUserId) && project.getDeletedBy() == null)
				.count();

		List<ProjectDto> user1SavedProjects = projectService.getAllProjectsByUserId(testUserId);

		assertNotNull(user1SavedProjects);
		assertEquals(user1ProjectsExcludingSoftDeletedCount, user1SavedProjects.size());
	}

	@Test
	public void testGetProjectByIdWhereProjectIsExisting() {
		ProjectDto projectDto = projectService.getProjectById(1L);
		assertNotNull(projectDto);
	}

	@Test
	public void testGetProjectByIdWhereProjectIsNotExisting() {
		ProjectDto projectDto = projectService.getProjectById(100L);
		assertNull(projectDto);
	}

	@Test
	public void testGetProjectByIdWhereProjectIsSoftDeleted() {
		ProjectDto projectDto = projectService.getProjectById(4L);
		assertNull(projectDto);
	}

	@Test
	public void testCreateProject() {
		long savedProjectsCount = projectRepository.findAll().size();

		ProjectDto projectDto = new ProjectDto()
				.setName("new project")
				.setDescription("a project that does new things")
				.setUserDto(UserMapper.mapEntityToDto(ProjectTestData.testUser1));
		projectService.createProject(projectDto);

		ProjectDto createdProjectDto = projectService.getProjectById(savedProjectsCount + 1);
		assertNotNull(createdProjectDto);
	}

	@Test
	public void testCreateProjectWhereDtoHasNonNullId() {
		ProjectDto projectDto = new ProjectDto()
				.setId(1L)
				.setName("new project")
				.setDescription("a project that does new things")
				.setUserDto(UserMapper.mapEntityToDto(ProjectTestData.testUser1));
		assertThrows(RuntimeException.class, () -> projectService.createProject(projectDto));
	}

	@Test
	public void testSoftDeleteProjectByIdWhereProjectIsExisting() {
		boolean isDeleted = projectService.softDeleteProjectById(1L);
		assertTrue(isDeleted);
	}

	@Test
	public void testSoftDeleteProjectByIdWhereProjectIsNotExisting() {
		boolean isDeleted = projectService.softDeleteProjectById(100L);
		assertFalse(isDeleted);
	}

	@Test
	public void testUpdateUser() {
		ProjectDto projectDto = new ProjectDto()
				.setId(1L)
				.setName("very good project")
				.setDescription("a project that does good things")
				.setUserDto(UserMapper.mapEntityToDto(ProjectTestData.testUser1));
		ProjectDto updatedProjectDto = projectService.updateProjectById(1L, projectDto);
		assertEquals(projectDto.getName(), updatedProjectDto.getName());
	}

	@Test
	public void testUpdateUserWhereIdArgAndDtoIdMismatch() {
		ProjectDto projectDto = new ProjectDto()
				.setId(1L)
				.setName("very good project")
				.setDescription("a project that does good things")
				.setUserDto(UserMapper.mapEntityToDto(ProjectTestData.testUser1));
		assertThrows(RuntimeException.class, () -> projectService.updateProjectById(2L, projectDto));
	}
}
