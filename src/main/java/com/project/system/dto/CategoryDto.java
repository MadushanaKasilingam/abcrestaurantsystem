package com.project.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CategoryDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Restaurant ID is mandatory")
    private Long restaurant_id;
}