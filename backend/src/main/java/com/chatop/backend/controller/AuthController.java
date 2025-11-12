package com.chatop.backend.controller;

import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.security.dto.AuthenticationRequest;
import com.chatop.backend.security.dto.AuthenticationResponse;
import com.chatop.backend.security.dto.RegisterRequest;
import com.chatop.backend.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registers")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        System.out.println("📩 Register received: " + request.getEmail() + " - " + request.getName());
       // return authService.register(request);
        return null;
    }


    // ✅ LOGIN
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authService.login(request);
    }

    // ✅ ME (Profil utilisateur connecté)
    @GetMapping("/me")
    public User me(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        String email = authService.getEmailFromToken(token);
        return userRepository.findByEmail(email).orElseThrow();
    }
}
