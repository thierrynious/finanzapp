package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.enums.CategoryType;
import com.finanzmanager.finanzapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Custom Query mit benanntem Parameter
    @Query("SELECT c FROM Category c WHERE c.type = :type")
    List<Category> findByType(@Param("type") CategoryType type);

    // Suche nach Name
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(@Param("name") String name);

    // LIKE-Suche mit Teilstring
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    List<Category> searchByKeyword(@Param("keyword") String keyword);


}
