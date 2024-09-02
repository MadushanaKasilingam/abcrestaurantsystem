package com.project.system.repository;

import com.project.system.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r WHERE lower(r.name) LIKE lower(concat('%', :query, '%')) OR lower(r.cuisineType) LIKE lower(concat('%', :query, '%'))")
    List<Restaurant> findBySearchQuery(@Param("query") String query);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Restaurant r WHERE r.name = :name AND r.address.streetAddress = :streetAddress AND r.address.city = :city AND r.address.stateProvince = :stateProvince AND r.address.postalCode = :postalCode AND r.address.country = :country")
    boolean existsByNameAndAddress(@Param("name") String name,
                                   @Param("streetAddress") String streetAddress,
                                   @Param("city") String city,
                                   @Param("stateProvince") String stateProvince,
                                   @Param("postalCode") String postalCode,
                                   @Param("country") String country);

    List<Restaurant> findByName(String name);
}