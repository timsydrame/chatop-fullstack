package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalRequest;
import com.chatop.backend.dto.RentalResponse;
import com.chatop.backend.service.AuthService;
import com.chatop.backend.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private AuthService authService;

    // POST /api/rentals
    @PostMapping
    public RentalResponse createRental(
            Principal principal,
            @Valid @RequestBody RentalRequest request
    ) {
        // email de l’utilisateur connecté
        String email = principal.getName();

        // récupère l’ID du user
        Long ownerId = authService.getUserIdFromEmail(email);

        return rentalService.createRental(request, ownerId);
    }

    // GET /api/rentals
    @GetMapping
    public List<RentalResponse> getAll() {
        return rentalService.getAllRentals();
    }

    // GET /api/rentals/{id}
    @GetMapping("/{id}")
    public RentalResponse getById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }
}
