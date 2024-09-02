package com.project.system.response;

import com.project.system.model.GalleryImage;
import lombok.Data;

@Data
public class GalleryImageResponse {

    private String message;
    private GalleryImage image;

    public GalleryImageResponse(String message, GalleryImage image) {
        this.message = message;
        this.image = image;
    }

    // Getters and setters
}