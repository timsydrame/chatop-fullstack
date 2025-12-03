package com.chatop.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Chatop API",
                version = "1.0",
                description = "API de gestion des locations, utilisateurs et messages pour le projet Chatop.",
                contact = @Contact(
                        name = "Chatop Support",
                        email = "support@chatop.com"
                ),
                license = @License(
                        name = "MIT"
                )
        )
)
public class OpenApiConfig {
}

