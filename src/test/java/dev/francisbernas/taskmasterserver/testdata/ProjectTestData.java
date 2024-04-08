package dev.francisbernas.taskmasterserver.testdata;


import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectTestData {
	public static User testUser1 = (User) new User()
			.setId(1L)
			.setUsername("johnDoe123")
			.setFirstName("John")
			.setLastName("Doe")
			.setPassword("f823hfas")
			.setCreatedBy("admin")
			.setCreatedDate(LocalDateTime.now());

	public static User testUser2 = (User) new User()
			.setId(2L)
			.setUsername("mGrace123")
			.setFirstName("Mary")
			.setLastName("Grace")
			.setPassword("h283gf2")
			.setCreatedBy("admin")
			.setCreatedDate(LocalDateTime.now());

	public static List<Project> getProjectsTestData = List.of(
			(Project) new Project()
					.setId(1L)
					.setName("good project")
					.setDescription("a project that does good things")
					.setUser(testUser1)
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(Project) new Project()
					.setId(2L)
					.setName("bad project")
					.setDescription("a project that does bad things")
					.setUser(testUser1)
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(Project) new Project()
					.setId(3L)
					.setName("mundane project")
					.setDescription("a project that does mundane things")
					.setUser(testUser1)
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(Project) new Project()
					.setId(4L)
					.setName("common project")
					.setDescription("a project that does common things")
					.setUser(testUser1)
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now())
					.setDeletedBy("admin")
					.setDeletedDate(LocalDateTime.now())
	);
}
