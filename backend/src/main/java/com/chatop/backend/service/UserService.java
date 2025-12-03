package com.chatop.backend.service;

import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.dto.RegisterRequest;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.exeption.InvalidCredentialsException;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserInfo(String email) {

            User user = userRepository.findByEmail(email).orElseThrow();
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            response.setCreatedAt(user.getCreatedAt().format(formatter) );
            response.setUpdatedAt(user.getUpdatedAt().format(formatter)); // valeur par défaut attendue

            return response;
        }
    }

