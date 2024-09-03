package com.project.system.controller;

import com.project.system.model.Facility;
import com.project.system.response.ApiResponse;
import com.project.system.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchFacilities(@RequestParam String keyword) {
        ApiResponse response = new ApiResponse();

        if (keyword == null || keyword.trim().isEmpty()) {
            response.setMessage("Search keyword is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<Facility> facilities = facilityService.searchFacilities(keyword);
        response.setMessage("Facilities retrieved successfully");
        response.setData(facilities);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getFacilityById(@PathVariable Long id) {
        ApiResponse response = new ApiResponse();

        Facility facility = facilityService.getFacilityById(id);
        if (facility == null) {
            response.setMessage("Facility not found with ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setMessage("Facility retrieved successfully");
        response.setData(facility);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // New endpoint to get all facilities
    @GetMapping
    public ResponseEntity<ApiResponse> getAllFacilities() {
        ApiResponse response = new ApiResponse();

        List<Facility> facilities = facilityService.getAllFacilities();
        response.setMessage("All facilities retrieved successfully");
        response.setData(facilities);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse> getFacilitiesByRestaurantId(@PathVariable Long restaurantId) {
        ApiResponse response = new ApiResponse();

        if (restaurantId == null) {
            response.setMessage("Restaurant ID is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<Facility> facilities = facilityService.getFacilitiesByRestaurantId(restaurantId);
        if (facilities == null || facilities.isEmpty()) {
            response.setMessage("No facilities found for restaurant ID: " + restaurantId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setMessage("Facilities retrieved successfully");
        response.setData(facilities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}


