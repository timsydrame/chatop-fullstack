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

    @PostMapping
    public RentalResponse createRental(
            Principal principal,
            @Valid @RequestBody RentalRequest request
    ) {
        //String token = auth.substring(7);
        //String email = authService.getEmailFromToken(token);
        //Long userId = authService.getUserIdFromEmail(email);

        //return rentalService.createRental(request, userId);
        return null;
    }

    @GetMapping
    public List<RentalResponse> getAll() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public RentalResponse getById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }
}


