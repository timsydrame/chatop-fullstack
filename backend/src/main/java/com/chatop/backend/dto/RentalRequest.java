package com.chatop.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalRequest {

    @NotBlank
    private String name;

    private Double surface;
    private Double price;
    private String description;
    private String picture;
}

