package com.project.system.service;

import com.project.system.model.Category;
import com.project.system.model.Restaurant;
import com.project.system.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        if (categoryRepository.existsByNameAndRestaurantId(name, restaurantId)) {
            throw new Exception("Category with the same name already exists for this restaurant");
        }

        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Restaurant ID cannot be null");
        }
        return categoryRepository.findByRestaurantId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new Exception("Category not found");
        }
        return optionalCategory.get();
    }

    @Override
    public Category updateCategory(Long id, String name, Long restaurantId) throws Exception {
        Category category = getCategoryById(id);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }

        if (categoryRepository.existsByNameAndRestaurantId(name, restaurantId)) {
            throw new Exception("Category with the same name already exists for this restaurant");
        }

        category.setName(name);
        category.setRestaurant(restaurant);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws Exception {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        // Return all categories
        return categoryRepository.findAll();
    }

}
