package com.project.system.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationRequest {
    private Long restaurantId;           // Maps to Restaurant entity
    private Long userId;                 // Maps to User entity
    private Long DineinTableId;                // Maps to DineinTable entity
    private LocalDateTime reservationTime; // Maps to reservationTime in Reservation entity
    private int numberOfGuests;          // Maps to numberOfGuests in Reservation entity
    private String specialRequests;      // Maps to specialRequests in Reservation entity
    private String paymentStatus;        // Maps to paymentStatus in Reservation entity
    private LocalDateTime endTime;       // Maps to endTime in Reservation entity
}