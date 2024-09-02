package com.project.system.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gallery_images")
public class GalleryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    // Getters and setters
}