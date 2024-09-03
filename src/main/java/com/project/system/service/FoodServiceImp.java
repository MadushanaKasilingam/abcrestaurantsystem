package com.project.system.service;

import com.project.system.dto.CategoryDto;
import com.project.system.model.Category;
import com.project.system.model.Food;
import com.project.system.model.Restaurant;
import com.project.system.repository.CategoryRepository;
import com.project.system.repository.FoodRepository;
import com.project.system.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Food createFood(CreateFoodRequest request, Restaurant restaurant, Category category) throws Exception {
        if (restaurant == null) {
            throw new Exception("Restaurant cannot be null");
        }

        if (category == null) {
            throw new Exception("Category cannot be null");
        }

        Food food = new Food();
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImages(request.getImages());
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setVegetarian(request.isVegetarian());
        food.setSeasonal(request.isSeasonal());
        food.setAvailable(true);
        food.setCreatedDate(new Date());

        return foodRepository.save(food);
    }

    @Override
    public void deleteFood(Long id) throws Exception {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isEmpty()) {
            throw new Exception("Food not found");
        }
        foodRepository.deleteById(id);
    }

    @Override
    public Food updateAvailabilityStatus(Long id) throws Exception {
        Optional<Food> foodOptional = foodRepository.findById(id);
        if (foodOptional.isEmpty()) {
            throw new Exception("Food not found");
        }
        Food food = foodOptional.get();
        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }

    @Override
    public List<Food> searchFood(String keyword) throws Exception {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId, boolean vegetarian, boolean seasonal, boolean nonveg, String foodCategory) throws Exception {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (vegetarian) {
            foods = foods.stream().filter(Food::isVegetarian).collect(Collectors.toList());
        }
        if (seasonal) {
            foods = foods.stream().filter(Food::isSeasonal).collect(Collectors.toList());
        }
        if (nonveg) {
            foods = foods.stream().filter(food -> !food.isVegetarian()).collect(Collectors.toList());
        }
        if (foodCategory != null) {
            foods = foods.stream().filter(food -> food.getFoodCategory().getName().equalsIgnoreCase(foodCategory)).collect(Collectors.toList());
        }

        return foods;
    }

    @Override
    public List<Food> getAllFoods() throws Exception {
        return foodRepository.findAll();
    }

    @Override
    public Food getFoodById(Long id) throws Exception {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isPresent()) {
            return food.get();
        } else {
            throw new Exception("Food not found");
        }
    }

    @Override
    public Food updateFood(Long id, CreateFoodRequest foodRequest) throws Exception {
        Optional<Food> foodOptional = foodRepository.findById(id);
        if (foodOptional.isEmpty()) {
            throw new Exception("Food not found");
        }
        Food food = foodOptional.get();
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setImages(foodRequest.getImages());
        food.setVegetarian(foodRequest.isVegetarian());
        food.setSeasonal(foodRequest.isSeasonal());

        return foodRepository.save(food);
    }


}
