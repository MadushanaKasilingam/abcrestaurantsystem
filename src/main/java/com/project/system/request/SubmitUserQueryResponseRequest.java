package com.project.system.request;

import lombok.Data;

@Data
public class SubmitUserQueryResponseRequest {

    private Long queryId;
    private Long responderId; // ID of the staff responding
    private String responseMessage;


    public SubmitUserQueryResponseRequest() {}

    public SubmitUserQueryResponseRequest(Long queryId, Long responderId, String responseMessage) {
        this.queryId = queryId;
        this.responderId = responderId;
        this.responseMessage = responseMessage;
    }

    // Getters and Setters
}