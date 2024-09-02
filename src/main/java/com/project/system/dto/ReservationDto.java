package com.project.system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {

    private Long restaurantId;
    private Long userId;
    private LocalDateTime reservationTime;
    private int numberOfGuests;
    private String specialRequests;
}
