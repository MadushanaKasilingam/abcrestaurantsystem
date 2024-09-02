package com.project.system.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {

    private String name;
    private String description;
    private Long price;

    private Long foodCategoryId;
    private List<String> images;

    private Long restaurantId;
    private boolean isVegetarian;
    private boolean isSeasonal;
}


