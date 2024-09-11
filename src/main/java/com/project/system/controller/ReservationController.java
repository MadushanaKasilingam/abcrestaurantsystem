package com.project.system.controller;

import com.project.system.model.DineinTable;
import com.project.system.model.Reservation;
import com.project.system.response.MessageResponse;
import com.project.system.service.ReservationService;
import com.project.system.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TableService tableService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            // Check for duplicate reservation
            if (reservationService.isDuplicateReservation(reservation)) {
                return new ResponseEntity<>(new MessageResponse("Duplicate reservation detected"), HttpStatus.CONFLICT);
            }
            // Create the reservation
            Reservation createdReservation = reservationService.createReservation(reservation);

            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findReservationById(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.findReservationById(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse res = new MessageResponse();
            res.setMessage(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Reservation>> getAllReservationsForRestaurant(@PathVariable Long restaurantId) {
        List<Reservation> reservations = reservationService.getAllReservationsForRestaurant(restaurantId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getAllReservationsForUser(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getAllReservationsForUser(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/available-tables/{restaurantId}")
    public ResponseEntity<List<DineinTable>> getAvailableTablesForRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam("reservationTime") String reservationTimeStr,
            @RequestParam("duration") int duration) {

        LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeStr);
        List<DineinTable> availableTables = tableService.getAvailableTablesForRestaurant(restaurantId, reservationTime, duration);

        if (availableTables.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }
}
