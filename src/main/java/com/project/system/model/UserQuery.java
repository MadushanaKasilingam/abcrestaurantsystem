package com.project.system.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String subject;

    private String message;

    private LocalDateTime submissionTime;

    // Constructors, Getters, and Setters

    public UserQuery() {}

    public UserQuery(Long userId, String subject, String message) {
        this.userId = userId;
        this.subject = subject;
        this.message = message;
        this.submissionTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }
}