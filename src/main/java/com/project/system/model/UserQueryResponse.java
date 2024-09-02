package com.project.system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserQueryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "query_id", nullable = false)
    private UserQuery userQuery;

    @ManyToOne
    @JoinColumn(name = "responded_by", nullable = false)
    private User respondedBy; // Assuming you have a User entity

    private String responseMessage;

    private LocalDateTime responseTime;


    public UserQueryResponse() {}

    public UserQueryResponse(UserQuery userQuery, User respondedBy, String responseMessage) {
        this.userQuery = userQuery;
        this.respondedBy = respondedBy;
        this.responseMessage = responseMessage;
        this.responseTime = LocalDateTime.now();
    }


}