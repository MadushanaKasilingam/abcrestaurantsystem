package com.project.system.service;

import com.project.system.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name, Long restaurantId) throws Exception;

    List<Category> findCategoryByRestaurantId(Long id) throws Exception;

    Category getCategoryById(Long id) throws Exception;

    Category updateCategory(Long id, String name, Long restaurantId) throws Exception;

    void deleteCategory(Long id) throws Exception;

    List<Category> getAllCategories() throws Exception;
}
