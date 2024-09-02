package com.project.system.service;

import com.project.system.model.Offer;
import com.project.system.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfferValidator {

    @Autowired
    private OfferRepository offerRepository;

    public void validateOffer(Offer offer, Long restaurantId) {
        boolean exists = offerRepository.existsByTitleAndRestaurantId(offer.getTitle(), restaurantId);
        if (exists) {
            throw new IllegalArgumentException("An offer with the same title already exists for this restaurant");
        }
    }
}
