package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageRequest;
import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// Swagger / OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
@Tag(name = "Messages", description = "Envoi de messages entre utilisateurs")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @Operation(
            summary = "Envoyer un message",
            description = "Permet à un utilisateur authentifié d'envoyer un message (par exemple au propriétaire d'une location). "
                    + "Le contenu du message est fourni dans le corps de la requête."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données du message invalides"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
            @ApiResponse(responseCode = "404", description = "Ressource associée non trouvée (ex : location)")
    })
    public MessageResponse createMessage(
            @Parameter(hidden = true)
            Principal principal,
            @Parameter(
                    description = "Données du message à envoyer",
                    required = true
            )
            @Valid @RequestBody MessageRequest request
    ) {
        String email = principal.getName(); // vient du JWT
        return messageService.createMessage(request, email);
    }
}
