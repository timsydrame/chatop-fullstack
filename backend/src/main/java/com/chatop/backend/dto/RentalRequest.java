package com.chatop.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile picture;
}
