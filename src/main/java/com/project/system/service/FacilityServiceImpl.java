package com.project.system.service;

import com.project.system.dto.FacilityResponseDTO;
import com.project.system.model.Facility;
import com.project.system.model.Restaurant;
import com.project.system.repository.FacilityRepository;
import com.project.system.repository.RestaurantRepository;
import com.project.system.request.FacilityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;



    @Override
    public Facility createFacility(FacilityRequest facilityRequest) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(facilityRequest.getRestaurantId());
        if (restaurantOpt.isEmpty()) {
            throw new RuntimeException("Restaurant not found with ID: " + facilityRequest.getRestaurantId());
        }

        if (facilityRepository.existsByNameAndRestaurant(facilityRequest.getName(), restaurantOpt.get())) {
            throw new IllegalArgumentException("Facility already exists for this restaurant");
        }

        Facility facility = new Facility();
        facility.setName(facilityRequest.getName());
        facility.setDescription(facilityRequest.getDescription());
        facility.setRestaurant(restaurantOpt.get());

        return facilityRepository.save(facility);
    }

    @Override
    public Facility updateFacility(Long id, FacilityRequest facilityRequest) {
        Optional<Facility> facilityOpt = facilityRepository.findById(id);
        if (facilityOpt.isEmpty()) {
            throw new IllegalArgumentException("Facility not found");
        }

        Facility facility = facilityOpt.get();

        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(facilityRequest.getRestaurantId());
        if (restaurantOpt.isEmpty()) {
            throw new RuntimeException("Restaurant not found with ID: " + facilityRequest.getRestaurantId());
        }

        if (facilityRepository.existsByNameAndRestaurant(facilityRequest.getName(), restaurantOpt.get()) &&
                !facility.getName().equals(facilityRequest.getName())) {
            throw new IllegalArgumentException("Facility already exists for this restaurant");
        }

        facility.setName(facilityRequest.getName());
        facility.setDescription(facilityRequest.getDescription());
        facility.setRestaurant(restaurantOpt.get());

        return facilityRepository.save(facility);
    }

    @Override
    public boolean deleteFacility(Long id) {
        Optional<Facility> facilityOpt = facilityRepository.findById(id);
        if (facilityOpt.isPresent()) {
            facilityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<FacilityResponseDTO> searchFacilities(String keyword) {
        return facilityRepository.findByNameContaining(keyword).stream()
                .map(facility -> new FacilityResponseDTO(
                        facility.getName(),
                        facility.getDescription(),
                        facility.getRestaurant().getName())) // Assuming getRestaurant() returns the Restaurant object
                .collect(Collectors.toList());
    }

    @Override
    public List<Facility> getFacilitiesByRestaurantId(Long restaurantId) {
        return facilityRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id).orElse(null);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }
}
