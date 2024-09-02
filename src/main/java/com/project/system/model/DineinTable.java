package com.project.system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Data
public class DineinTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;
    private Integer tableNumber;
    private Integer seats;
    private Boolean available; // Ensure this field is present



    public Boolean isAvailable() {  // Method name should be 'isAvailable' for Boolean

        return available;
    }

}