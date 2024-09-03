package com.project.system.controller;

import com.project.system.model.Food;
import com.project.system.model.User;
import com.project.system.service.FoodService;
import com.project.system.service.UserService;
import com.project.system.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    // Search food by name
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foods = foodService.searchFood(name);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    // Get all foods by restaurant and optional category
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Boolean seasonal,
            @RequestParam(required = false) Boolean nonveg,
            @RequestParam(required = false) String foodCategory,
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        // Handle null values for optional parameters
        Boolean finalVegetarian = vegetarian != null ? vegetarian : false;
        Boolean finalSeasonal = seasonal != null ? seasonal : false;
        Boolean finalNonveg = nonveg != null ? nonveg : false;

        List<Food> foods = foodService.getRestaurantsFood(restaurantId, finalVegetarian, finalNonveg, finalSeasonal, foodCategory);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    // Get all foods
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods(@RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserByJwtToken(jwt);
        List<Food> foods = foodService.getAllFoods();
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    // Get food by ID
    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) throws Exception {
        Food food = foodService.getFoodById(id);
        if (food != null) {
            return new ResponseEntity<>(food, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
