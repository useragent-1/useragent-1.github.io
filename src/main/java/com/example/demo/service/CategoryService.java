package com.example.demo.service;

import com.example.demo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAllCategories();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
} 