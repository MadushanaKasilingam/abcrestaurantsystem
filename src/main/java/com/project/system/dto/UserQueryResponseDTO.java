package com.project.system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserQueryResponseDTO {

    private Long queryId;
    private String responderName; // Assuming you have a User entity with a name
    private String responseMessage;
    private LocalDateTime responseTime;

    // Constructors, Getters, and Setters

    public UserQueryResponseDTO() {}

    public UserQueryResponseDTO(Long queryId, String responderName, String responseMessage, LocalDateTime responseTime) {
        this.queryId = queryId;
        this.responderName = responderName;
        this.responseMessage = responseMessage;
        this.responseTime = responseTime;
    }

    // Getters and Setters
}