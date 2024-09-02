package com.project.system.request;


public class SubmitUserQueryRequest {

    private Long userId;
    private String subject;
    private String message;

    // Constructors, Getters, and Setters

    public SubmitUserQueryRequest() {}

    public SubmitUserQueryRequest(Long userId, String subject, String message) {
        this.userId = userId;
        this.subject = subject;
        this.message = message;
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
}