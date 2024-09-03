package com.project.system.response;

import com.project.system.model.Offer;

import java.util.List;

public class OfferListResponse {
    private String message;
    private List<Offer> offers;

    // Constructor
    public OfferListResponse() {}

    public OfferListResponse(String message, List<Offer> offers) {
        this.message = message;
        this.offers = offers;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
