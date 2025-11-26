package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.Category;
import com.finanzmanager.finanzapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }
    public List<Category> findAll() {
        return repo.findAll();
    }
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }
    public Category create(Category category) {
        return repo.save(category);
    }

    public Optional<Category> update(Long id, Category newCategory) {
        return repo.findById(id).map(existing -> {
            existing.setName(newCategory.getName());
            existing.setType(newCategory.getType());
            // Weitere Felder bei Bedarf hinzufügen
            return repo.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}



