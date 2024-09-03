package com.project.system.controller;

import com.project.system.model.Offer;
import com.project.system.response.MessageResponse;
import com.project.system.response.OfferListResponse;
import com.project.system.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/offers")
public class AdminOfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createOffer(@PathVariable Long restaurantId, @Valid @RequestBody Offer offer, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        offerService.createOffer(offer, restaurantId);
        return ResponseEntity.ok(new MessageResponse("Offer created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOffer(@PathVariable Long id, @Valid @RequestBody Offer offer, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        offerService.updateOffer(id, offer);
        return ResponseEntity.ok(new MessageResponse("Offer updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        MessageResponse res = new MessageResponse("Offer deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OfferListResponse> getAllOffersByRestaurant(@PathVariable Long restaurantId) {
        OfferListResponse response = new OfferListResponse();

        if (restaurantId == null) {
            response.setMessage("Restaurant ID is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<Offer> offers = offerService.getAllOffersByRestaurant(restaurantId);
        if (offers == null || offers.isEmpty()) {
            response.setMessage("No offers found for restaurant ID: " + restaurantId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setMessage("Offers retrieved successfully");
        response.setOffers(offers);  // Corrected this line
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getOfferById(@PathVariable Long id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Offer not found"));
        }
        return ResponseEntity.ok(offer);
    }
}
