package com.project.system.service;

import com.project.system.model.Reservation;

public interface EmailService {
    void sendReservationConfirmationEmail(String to, Reservation reservation);
}
