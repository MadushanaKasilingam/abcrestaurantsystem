package com.project.system.repository;

import com.project.system.model.Facility;
import com.project.system.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);

    List<Facility> findByNameContaining(String keyword);
    

    List<Facility> findByRestaurantId(Long restaurantId);

    // New method to find all facilities
    List<Facility> findAll();
}
