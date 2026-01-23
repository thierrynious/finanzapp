// src/main/java/com/finanzmanager/finanzapp/repository/UserRepository.java
package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
