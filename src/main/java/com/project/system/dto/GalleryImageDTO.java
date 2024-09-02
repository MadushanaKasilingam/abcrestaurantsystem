package com.project.system.dto;

import lombok.Data;

@Data
public class GalleryImageDTO {

    private Long id;
    private String imageUrl;
    private Long restaurantId;

    // Add any other fields you need

    // Constructor matching the parameters (Long id, String imageUrl, Long restaurantId)
    public GalleryImageDTO(Long id, String imageUrl, Long restaurantId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
    }

    // Default constructor
    public GalleryImageDTO() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    // Add any other methods you need
}