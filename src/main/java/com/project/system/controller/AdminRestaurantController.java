package com.project.system.controller;

import com.project.system.model.Restaurant;
import com.project.system.request.CreateRestaurantRequest;
import com.project.system.response.MessageResponse;
import com.project.system.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<MessageResponse> createRestaurant(
            @RequestBody CreateRestaurantRequest req
    ) throws Exception {
        Restaurant restaurant = restaurantService.createRestaurant(req);
        return new ResponseEntity<>(new MessageResponse("Restaurant added successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @PathVariable Long id
    ) throws Exception {
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return new ResponseEntity<>(new MessageResponse("Restaurant updated successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
            @PathVariable Long id
    ) throws Exception {
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>(new MessageResponse("Restaurant deleted successfully"), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MessageResponse> updateRestaurantStatus(
            @PathVariable Long id
    ) throws Exception {
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(new MessageResponse("Restaurant status updated successfully"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @PathVariable Long id
    ) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}