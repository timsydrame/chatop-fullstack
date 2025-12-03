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
            @ModelAttribute @Valid RentalRequest rentalRequest
    ) throws IOException {

        // Récupérer l'utilisateur connecté via Principal


        return rentalService.createRental(rentalRequest, principal.getName());
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

    @PutMapping(value="/{id}", consumes = "multipart/form-data")
    public RentalResponse updateRental(
            Principal principal,
            @PathVariable Long id,
            @ModelAttribute RentalRequest rentalRequest
    ) throws IOException {
        return rentalService.updateRental(id, rentalRequest, principal.getName());
    }


}
