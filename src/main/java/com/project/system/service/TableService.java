package com.project.system.service;

import com.project.system.model.DineinTable;
import java.time.LocalDateTime;
import java.util.List;

public interface TableService {
    DineinTable createDineinTable(DineinTable dineinTable) throws Exception;
    DineinTable findTableById(Long id) throws Exception;
    List<DineinTable> getAllTables();
    void updateTableAvailability(Long id, boolean isAvailable) throws Exception;
    boolean isTableAvailable(Long tableId, LocalDateTime reservationTime, int duration) throws Exception;
    DineinTable save(DineinTable dineinTable);
    boolean isTableDuplicate(Long restaurantId, Integer tableNumber);
    void deleteTable(Long id) throws Exception;
    List<DineinTable> findTablesByRestaurantName(String restaurantName);
    DineinTable updateDineinTable(DineinTable dineinTable) throws Exception;


    // New method to be added
    List<DineinTable> getAvailableTablesForRestaurant(Long restaurantId, LocalDateTime reservationTime, int duration);
}
