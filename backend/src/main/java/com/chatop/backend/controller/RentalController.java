package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalRequest;
import com.chatop.backend.dto.RentalResponse;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.AuthService;
import com.chatop.backend.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

// Swagger / OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin
@Tag(name = "Rentals", description = "Gestion des annonces de locations")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    // POST /api/rentals
    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Créer une nouvelle location",
            description = "Création d'une nouvelle annonce de location pour l'utilisateur connecté. "
                    + "Les données sont envoyées en multipart/form-data (titre, description, prix, image, etc.)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Location créée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })
    public RentalResponse createRental(
            @Parameter(hidden = true)
            Principal principal,
            @Parameter(
                    description = "Données de la location à créer (multipart/form-data)"
            )
            @ModelAttribute @Valid RentalRequest rentalRequest
    ) throws IOException {
        return rentalService.createRental(rentalRequest, principal.getName());
    }

    // GET /api/rentals
    @GetMapping
    @Operation(
            summary = "Lister toutes les locations",
            description = "Retourne la liste de toutes les annonces de locations disponibles."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des locations récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RentalResponse.class))
            )
    )
    public List<RentalResponse> getAll() {
        return rentalService.getAllRentals();
    }

    // GET /api/rentals/{id}
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une location par son identifiant",
            description = "Retourne le détail d'une annonce de location à partir de son id."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Location trouvée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    public RentalResponse getById(
            @Parameter(description = "Identifiant de la location", example = "1")
            @PathVariable Long id
    ) {
        return rentalService.getRentalById(id);
    }

    // PUT /api/rentals/{id}
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(
            summary = "Mettre à jour une location",
            description = "Met à jour une annonce de location existante pour l'utilisateur connecté. "
                    + "Les données sont envoyées en multipart/form-data."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Location mise à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
            @ApiResponse(responseCode = "404", description = "Location non trouvée")
    })
    public RentalResponse updateRental(
            @Parameter(hidden = true)
            Principal principal,
            @Parameter(description = "Identifiant de la location à modifier", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la location (multipart/form-data)")
            @ModelAttribute RentalRequest rentalRequest
    ) throws IOException {
        return rentalService.updateRental(id, rentalRequest, principal.getName());
    }

}
