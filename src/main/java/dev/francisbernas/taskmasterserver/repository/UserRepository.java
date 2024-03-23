package dev.francisbernas.taskmasterserver.repository;

import dev.francisbernas.taskmasterserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
