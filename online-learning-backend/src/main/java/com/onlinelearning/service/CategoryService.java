package com.onlinelearning.service;

import com.onlinelearning.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    void saveCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Long id);
}
