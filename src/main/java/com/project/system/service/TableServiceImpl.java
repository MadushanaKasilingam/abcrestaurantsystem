package com.project.system.service;

import com.project.system.model.DineinTable;
import com.project.system.model.Restaurant;
import com.project.system.model.Reservation;
import com.project.system.repository.TableRepository;
import com.project.system.repository.ReservationRepository;
import com.project.system.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public DineinTable save(DineinTable dineinTable) {
        return tableRepository.save(dineinTable);
    }

    @Override
    public DineinTable createDineinTable(DineinTable dineinTable) throws Exception {
        Restaurant restaurant = restaurantRepository.findById(dineinTable.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id " + dineinTable.getRestaurantId()));

        if (isTableDuplicate(dineinTable.getRestaurantId(), dineinTable.getTableNumber())) {
            throw new Exception("Table with the same number already exists in this restaurant");
        }

        dineinTable.setRestaurantId(restaurant.getId());
        return save(dineinTable);
    }

    @Override
    public DineinTable findTableById(Long id) throws Exception {
        return tableRepository.findById(id)
                .orElseThrow(() -> new Exception("DineinTable not found with id " + id));
    }

    @Override
    public List<DineinTable> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public void updateTableAvailability(Long id, boolean isAvailable) throws Exception {
        DineinTable dineinTable = findTableById(id);
        dineinTable.setAvailable(isAvailable);
        save(dineinTable);
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime reservationTime, int duration) throws Exception {
        DineinTable dineinTable = findTableById(tableId);

        if (dineinTable == null || !dineinTable.isAvailable()) {
            return false;
        }

        LocalDateTime endTime = reservationTime.plusHours(duration);

        List<Reservation> reservations = reservationRepository.findByDineinTable_Id(tableId);
        for (Reservation reservation : reservations) {
            if (reservation.getReservationTime().isBefore(endTime) && reservation.getEndTime().isAfter(reservationTime)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isTableDuplicate(Long restaurantId, Integer tableNumber) {
        return tableRepository.existsByRestaurantIdAndTableNumber(restaurantId, tableNumber);
    }

    @Override
    public void deleteTable(Long id) throws Exception {
        DineinTable dineinTable = findTableById(id);
        if (dineinTable == null) {
            throw new Exception("DineinTable not found with id " + id);
        }
        tableRepository.delete(dineinTable);
    }

    @Override
    public List<DineinTable> findTablesByRestaurantName(String restaurantName) {
        List<Restaurant> restaurants = restaurantRepository.findByName(restaurantName);
        if (restaurants.isEmpty()) {
            return List.of(); // No restaurant found with the given name
        }
        return tableRepository.findByRestaurantIdIn(
                restaurants.stream().map(Restaurant::getId).toList()
        );
    }

    @Override
    public DineinTable updateDineinTable(DineinTable dineinTable) throws Exception {
        DineinTable existingTable = findTableById(dineinTable.getId());

        if (existingTable == null) {
            throw new Exception("Table not found with id " + dineinTable.getId());
        }

        // Update fields (assuming all fields are updatable)
        existingTable.setAvailable(dineinTable.isAvailable());
        existingTable.setRestaurantId(dineinTable.getRestaurantId());
        existingTable.setSeats(dineinTable.getSeats());
        existingTable.setTableNumber(dineinTable.getTableNumber());

        // Save the updated table
        return save(existingTable);
    }

    @Override
    public List<DineinTable> getAvailableTablesForRestaurant(Long restaurantId, LocalDateTime reservationTime, int duration) {
        List<DineinTable> allTables = tableRepository.findByRestaurantId(restaurantId);

        return allTables.stream()
                .filter(table -> {
                    try {
                        return isTableAvailable(table.getId(), reservationTime, duration);
                    } catch (Exception e) {
                        // Handle exception (log or rethrow)
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DineinTable> findTablesByRestaurantId(Long restaurantId) {
        return tableRepository.findByRestaurantId(restaurantId);
    }

}
