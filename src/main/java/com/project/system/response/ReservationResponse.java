package com.project.system.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private Long id;
    private String restaurantName;
    private String userName;
    private LocalDateTime reservationTime;
    private int numberOfGuests;
    private String specialRequests;
}
