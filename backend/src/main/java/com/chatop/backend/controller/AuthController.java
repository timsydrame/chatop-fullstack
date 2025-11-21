package com.chatop.backend.controller;

import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.dto.RegisterRequest;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }



    // LOGIN
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authService.login(request);
    }

    //  ME (Profil utilisateur connecté)
    @GetMapping("/me")
    public UserResponse me(Principal principal) {


        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        if (user.getCreatedAt() == null) {
            response.setCreated_at("2022/02/02"); // valeur par défaut exigée par Mockoon
        } else {
            response.setCreated_at(user.getCreatedAt().format(formatter));
        }

        if (user.getUpdatedAt() == null) {
            response.setUpdated_at("2022/08/02"); // valeur par défaut attendue
        } else {
            response.setUpdated_at(user.getUpdatedAt().format(formatter));
        }

        return response;
    }

}
