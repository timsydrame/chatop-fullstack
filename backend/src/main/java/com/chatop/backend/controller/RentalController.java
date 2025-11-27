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

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    // POST /api/rentals
    @PostMapping(consumes = "multipart/form-data")
    public RentalResponse createRental(
            Principal principal,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture
    ) throws IOException {

        // Récupérer l'utilisateur connecté via Principal
        Long ownerId = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"))
                .getId();

        return rentalService.createRental(name, surface, price, description, picture, ownerId);
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
