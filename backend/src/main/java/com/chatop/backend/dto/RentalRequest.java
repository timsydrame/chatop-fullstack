package com.chatop.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double surface;

    @NotNull
    private Double price;

    @NotBlank
    private String description;

    // pour l’instant simple String (Mockoon accepte ça)
    @NotBlank
    private String picture;
}
