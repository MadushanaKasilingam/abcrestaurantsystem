package com.project.system.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddGalleryImageRequest {

    @NotEmpty(message = "Image URL must not be empty.")
    private String url;

    @NotNull(message = "Restaurant ID must not be null.")
    private Long restaurantId;

    // Getters and setters
}