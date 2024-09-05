package com.project.system.response;

import java.util.List;

public class ResponseData<T> {
    private List<T> data;
    private String message;

    // Constructors
    public ResponseData() {}

    public ResponseData(List<T> data) {
        this.data = data;
    }

    public ResponseData(List<T> data, String message) {
        this.data = data;
        this.message = message;
    }

    // Getters and Setters
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
