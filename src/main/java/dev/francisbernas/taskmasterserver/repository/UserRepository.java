package dev.francisbernas.taskmasterserver.repository;

import dev.francisbernas.taskmasterserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.deletedDate IS NULL")
    List<User> findAllExcludingSoftDeleted();

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.deletedDate IS NULL")
    Optional<User> findByIdNotSoftDeleted(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.deletedBy = :deletedBy, u.deletedDate = :deletedDate WHERE u.id = :userId")
    void softDeleteById(@Param("deletedBy") String deletedBy, @Param("deletedDate")LocalDateTime deletedDate, @Param("userId") Long userId);
}
