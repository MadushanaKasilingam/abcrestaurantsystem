package com.project.system.controller;

import com.project.system.model.Facility;
import com.project.system.request.FacilityRequest;
import com.project.system.response.ApiResponse;
import com.project.system.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/facility")
public class AdminFacilityController {

    @Autowired
    private FacilityService facilityService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createFacility(@RequestBody FacilityRequest facilityRequest) {
        ApiResponse response = new ApiResponse();

        // Validate the user's role
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Validate required fields
        if (facilityRequest.getName() == null || facilityRequest.getName().trim().isEmpty()) {
            response.setMessage("Facility name is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Facility facility = facilityService.createFacility(facilityRequest);
            response.setMessage("Facility created successfully");
            response.setData(facility);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateFacility(@PathVariable Long id, @RequestBody FacilityRequest facilityRequest) {
        ApiResponse response = new ApiResponse();

        // Validate the user's role
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Validate required fields
        if (facilityRequest.getName() == null || facilityRequest.getName().trim().isEmpty()) {
            response.setMessage("Facility name is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Facility facility = facilityService.updateFacility(id, facilityRequest);
            response.setMessage("Facility updated successfully");
            response.setData(facility);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFacility(@PathVariable Long id) {
        ApiResponse response = new ApiResponse();

        // Validate the user's role
        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.setMessage("You do not have permission to perform this action");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        boolean deleted = facilityService.deleteFacility(id);

        if (!deleted) {
            response.setMessage("Facility not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setMessage("Facility deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}