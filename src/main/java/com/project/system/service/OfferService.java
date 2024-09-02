package com.project.system.service;

import com.project.system.model.Offer;

import java.util.List;

public interface OfferService {
    Offer createOffer(Offer offer, Long restaurantId);
    Offer updateOffer(Long id, Offer offer);
    void deleteOffer(Long id);
    List<Offer> getAllOffersByRestaurant(Long restaurantId);
    Offer getOfferById(Long id); // New method
}
