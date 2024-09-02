package com.project.system.service;

import com.project.system.model.DineinTable;
import com.project.system.model.Reservation;
import com.project.system.model.Restaurant;
import com.project.system.model.User;
import com.project.system.repository.ReservationRepository;
import com.project.system.repository.TableRepository;
import com.project.system.repository.RestaurantRepository;
import com.project.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    @Override
    public Reservation createReservation(Reservation reservation) throws Exception {
        if (reservation.getDineinTable() == null || reservation.getDineinTable().getId() == null) {
            throw new Exception("DineinTable is required.");
        }

        // Validate and fetch Restaurant
        Restaurant restaurant = restaurantRepository.findById(reservation.getRestaurant().getId())
                .orElseThrow(() -> new Exception("Restaurant not found with id " + reservation.getRestaurant().getId()));

        // Validate and fetch User
        User user = userRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new Exception("User not found with id " + reservation.getUser().getId()));

        // Validate and fetch DineinTable
        DineinTable dineinTable = tableRepository.findById(reservation.getDineinTable().getId())
                .orElseThrow(() -> new Exception("DineinTable not found with id " + reservation.getDineinTable().getId()));

        // Set validated objects back to reservation
        reservation.setRestaurant(restaurant);
        reservation.setUser(user);
        reservation.setDineinTable(dineinTable);

        // Check for duplicate reservation
        if (isDuplicateReservation(reservation)) {
            throw new Exception("Duplicate reservation detected.");
        }

        // Save the Reservation
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findReservationById(Long id) throws Exception {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found with id " + id));
    }

    @Override
    public List<Reservation> getAllReservationsForRestaurant(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Reservation> getAllReservationsForUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public boolean isDuplicateReservation(Reservation reservation) {
        List<Reservation> reservations = reservationRepository.findByDineinTable_Id(reservation.getDineinTable().getId());
        for (Reservation r : reservations) {
            if (r.getReservationTime().equals(reservation.getReservationTime()) &&
                    r.getEndTime().equals(reservation.getEndTime()) &&
                    r.getUser().equals(reservation.getUser())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DineinTable> getAvailableTablesForRestaurant(Long restaurantId) {
        // Fetch available tables based on restaurant ID
        return tableRepository.findAvailableTablesByRestaurantId(restaurantId);
    }
}
