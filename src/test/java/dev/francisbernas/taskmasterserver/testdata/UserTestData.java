package dev.francisbernas.taskmasterserver.testdata;

import dev.francisbernas.taskmasterserver.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserTestData {
	public static List<User> getUsersTestData = List.of(
			(User) new User()
					.setId(1L)
					.setUsername("johnDoe123")
					.setFirstName("John")
					.setLastName("Doe")
					.setPassword("f823hfas")
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(User) new User()
					.setId(2L)
					.setUsername("mGrace123")
					.setFirstName("Mary")
					.setLastName("Grace")
					.setPassword("h283gf2")
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(User) new User()
					.setId(3L)
					.setUsername("elishaLara")
					.setFirstName("Elisha")
					.setLastName("Lara")
					.setPassword("nvdnvj82")
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now()),
			(User) new User()
					.setId(4L)
					.setUsername("joseLoro95")
					.setFirstName("Jose")
					.setLastName("Loro")
					.setPassword("1nhsad8fj")
					.setCreatedBy("admin")
					.setCreatedDate(LocalDateTime.now())
					.setDeletedBy("admin")
					.setDeletedDate(LocalDateTime.now())
	);
}
