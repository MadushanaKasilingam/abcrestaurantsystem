package com.project.system.dto;

public class FacilityResponseDTO {
    private String facilityName;
    private String facilityDescription;
    private String restaurantName;

    public FacilityResponseDTO(String facilityName, String facilityDescription, String restaurantName) {
        this.facilityName = facilityName;
        this.facilityDescription = facilityDescription;
        this.restaurantName = restaurantName;
    }

    // Getters and setters
    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityDescription() {
        return facilityDescription;
    }

    public void setFacilityDescription(String facilityDescription) {
        this.facilityDescription = facilityDescription;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
