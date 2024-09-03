package com.project.system.service;

import com.project.system.dto.FacilityResponseDTO;
import com.project.system.model.Facility;
import com.project.system.request.FacilityRequest;

import java.util.List;

public interface FacilityService {

    Facility createFacility(FacilityRequest facilityRequest);

    Facility updateFacility(Long id, FacilityRequest facilityRequest);

    boolean deleteFacility(Long id);

    List<FacilityResponseDTO> searchFacilities(String keyword);

    List<Facility> getFacilitiesByRestaurantId(Long restaurantId);

    Facility getFacilityById(Long id);

    // New method to get all facilities
    List<Facility> getAllFacilities();
}
