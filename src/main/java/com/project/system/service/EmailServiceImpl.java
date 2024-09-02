package com.project.system.service;

import com.project.system.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendReservationConfirmationEmail(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Confirmation");
        message.setText(String.format("Your reservation at %s is confirmed for %s.",
                reservation.getRestaurant().getName(), reservation.getReservationTime().toString()));
        mailSender.send(message);
    }
}
