package com.project.system.response;

import lombok.Data;

@Data
public class UserQueryResponseMessage {

    private String message;

    // Constructors, Getters, and Setters

    public UserQueryResponseMessage() {}

    public UserQueryResponseMessage(String message) {
        this.message = message;
    }

    // Getters and Setters
}