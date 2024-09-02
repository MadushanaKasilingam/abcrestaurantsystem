package com.project.system.service;

import com.project.system.model.Food;
import com.project.system.model.Category;
import com.project.system.model.Restaurant;
import com.project.system.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    Food createFood(CreateFoodRequest request, Restaurant restaurant, Category category) throws Exception;

    void deleteFood(Long id) throws Exception;

    Food updateAvailabilityStatus(Long id) throws Exception;

    List<Food> searchFood(String keyword) throws Exception;

    List<Food> getRestaurantsFood(Long restaurantId, boolean vegetarian, boolean seasonal, boolean nonveg, String foodCategory) throws Exception;

    List<Food> getAllFoods() throws Exception;

    Food getFoodById(Long id) throws Exception;

    Food updateFood(Long id, CreateFoodRequest foodRequest) throws Exception;
}
