package com.project.system.dto;

import com.project.system.model.Food;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class CategoryDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Restaurant ID is mandatory")
    private Long restaurant_id;
}
