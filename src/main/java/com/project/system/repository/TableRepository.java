package com.project.system.repository;

import com.project.system.model.DineinTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<DineinTable, Long> {
    boolean existsByRestaurantIdAndTableNumber(Long restaurantId, Integer tableNumber);

    List<DineinTable> findByRestaurantIdIn(List<Long> restaurantIds);
    List<DineinTable> findByRestaurantId(Long restaurantId);
    List<DineinTable> findAvailableTablesByRestaurantId(Long restaurantId);
}