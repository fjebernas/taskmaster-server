package dev.francisbernas.taskmasterserver.repository;

import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.deletedDate IS NULL")
    List<Project> findAllNotDeleted();

    @Query("SELECT p FROM Project p WHERE p.user = :user AND p.deletedDate IS NULL")
    List<Project> findAllByUserNotDeleted(@Param("user") User user);

    @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.deletedDate IS NULL")
    Optional<Project> findByIdNotDeleted(@Param("projectId") Long projectId);

    @Modifying
    @Query("UPDATE Project p SET p.deletedBy = :deletedBy, p.deletedDate = :deletedDate WHERE p.id = :projectId")
    void softDeleteById(@Param("deletedBy") String deletedBy, @Param("deletedDate") LocalDateTime deletedDate, @Param("projectId") Long projectId);
}
