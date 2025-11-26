package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.createdAt > ?1")
    List<User> findCreatedAfter(LocalDateTime time);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> loadByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    Optional<User> findNativeByEmail(String email);

    @Query("SELECT u.id AS id, u.username AS name FROM User u")
    List<Map<String, Object>> findAllSimplifiedUsers();

    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :id")
    int deactivateUser(@Param("id") Long id);
}
