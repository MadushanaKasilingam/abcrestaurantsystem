package com.project.system.response;


import com.project.system.model.User;
import com.project.system.model.UserQuery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    @Data
    @Entity
    public static class UserQueryResponse {

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
}