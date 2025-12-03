package com.chatop.backend.controller;

import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.dto.RegisterRequest;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.service.AuthService;
import com.chatop.backend.service.UserService;
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
@RequestMapping("/api/auth")
@CrossOrigin
@Tag(name = "Auth", description = "Authentification et gestion de l'utilisateur connecté")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @Operation(
            summary = "Inscription d'un nouvel utilisateur",
            description = "Permet de créer un nouveau compte utilisateur. "
                    + "Retourne un token JWT si l'inscription est réussie."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inscription réussie",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données d'inscription invalides"),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    public AuthenticationResponse register(
            @Parameter(
                    description = "Données d'inscription de l'utilisateur",
                    required = true
            )
            @RequestBody @Valid RegisterRequest request
    ) {
        return authService.register(request);
    }

    // LOGIN
    @PostMapping("/login")
    @Operation(
            summary = "Authentification (login)",
            description = "Permet à un utilisateur de se connecter avec son email et son mot de passe. "
                    + "Retourne un token JWT en cas de succès."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentification réussie",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données de connexion invalides"),
            @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
    })
    public AuthenticationResponse login(
            @Parameter(
                    description = "Identifiants de connexion (email et mot de passe)",
                    required = true
            )
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return authService.login(request);
    }

    //  ME (Profil utilisateur connecté)
    @GetMapping("/me")
    @Operation(
            summary = "Récupérer le profil de l'utilisateur connecté",
            description = "Retourne les informations du profil de l'utilisateur actuellement authentifié."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur connecté trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })
    public UserResponse me(
            @Parameter(hidden = true)
            Principal principal
    ) {
        return this.userService.getUserInfo(principal.getName());
    }

}
