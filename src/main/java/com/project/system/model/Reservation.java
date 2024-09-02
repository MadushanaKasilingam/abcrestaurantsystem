package com.project.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private User user;

    @ManyToOne
    private DineinTable dineinTable;

    private LocalDateTime reservationTime;

    private int numberOfGuests;

    private String specialRequests;

    private String paymentStatus;

    private LocalDateTime endTime;
}
