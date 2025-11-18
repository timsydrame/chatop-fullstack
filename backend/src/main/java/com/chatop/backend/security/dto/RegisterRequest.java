package com.chatop.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    @NotBlank(message = "Nom obligatoire")
    private String name;

    @NotBlank(message = "Mot de passe obligatoire")
    private String password;
}
