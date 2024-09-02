package com.project.system.controller;

import com.project.system.model.Category;
import com.project.system.model.Food;
import com.project.system.model.Restaurant;
import com.project.system.repository.CategoryRepository;
import com.project.system.repository.FoodRepository;
import com.project.system.repository.RestaurantRepository;
import com.project.system.request.CreateFoodRequest;
import com.project.system.response.MessageResponse;
import com.project.system.service.FoodService;
import com.project.system.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<MessageResponse> createFood(@RequestBody CreateFoodRequest request) {
        // Check for empty fields
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Food name is required"));
        }
        if (request.getPrice() == null || request.getPrice() <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Food price must be greater than zero"));
        }
        if (request.getRestaurantId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Restaurant ID is required"));
        }
        if (request.getFoodCategoryId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Category ID is required"));
        }

        // Log request for debugging
        System.out.println("Request isVegetarian: " + request.isVegetarian());
        System.out.println("Request isSeasonal: " + request.isSeasonal());

        // Check for duplicate food name in the same restaurant
        if (foodRepository.existsByNameAndRestaurantId(request.getName(), request.getRestaurantId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Food with this name already exists in the restaurant"));
        }

        try {
            // Create food
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));

            // Ensure the category exists
            Category category = categoryRepository.findById(request.getFoodCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

            Food food = foodService.createFood(request, restaurant, category);
            return ResponseEntity.ok(new MessageResponse("Food added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to add food"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            return ResponseEntity.ok(new MessageResponse("Food deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Food not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateFood(@PathVariable Long id, @RequestBody CreateFoodRequest request) {
        try {
            Food updatedFood = foodService.updateFood(id, request);
            return ResponseEntity.ok(new MessageResponse("Food updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Food not found"));
        }
    }
}
