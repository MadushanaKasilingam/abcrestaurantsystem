package com.project.system.service;

import com.project.system.model.DineinTable;
import com.project.system.model.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Reservation reservation) throws Exception;

    List<Reservation> getAllReservations();

    Reservation findReservationById(Long id) throws Exception;

    List<Reservation> getAllReservationsForRestaurant(Long restaurantId);

    List<Reservation> getAllReservationsForUser(Long userId);

    boolean isDuplicateReservation(Reservation reservation);

    // Add method for retrieving available tables for a restaurant
    List<DineinTable> getAvailableTablesForRestaurant(Long restaurantId);
}
