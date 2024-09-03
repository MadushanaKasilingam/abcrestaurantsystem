package com.project.system.service;

import com.project.system.model.Offer;
import com.project.system.model.Restaurant;
import com.project.system.repository.OfferRepository;
import com.project.system.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OfferValidator offerValidator;

    @Override
    public Offer createOffer(@Valid Offer offer, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        offerValidator.validateOffer(offer, restaurantId); // Validate before saving

        offer.setRestaurant(restaurant);
        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Long id, @Valid Offer offerDetails) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setTitle(offerDetails.getTitle());
        offer.setDescription(offerDetails.getDescription());
        offer.setDiscountPercentage(offerDetails.getDiscountPercentage());
        offer.setStartDate(offerDetails.getStartDate());
        offer.setEndDate(offerDetails.getEndDate());

        offerValidator.validateOffer(offer, offer.getRestaurant().getId()); // Validate before saving

        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public List<Offer> getAllOffersByRestaurant(Long restaurantId) {
        List<Offer> offers = offerRepository.findByRestaurantId(restaurantId);
        System.out.println("Offers found: " + offers.size());
        return offers;
    }


    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }
}
