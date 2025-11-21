package com.chatop.backend.exeption;

import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExeptionHandler {

    // 400 → Erreurs de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // 400 → Body manquant
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBodyMissing(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", "error"));
    }

    // 401 → Mauvais email/mot de passe
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials() {
        return ResponseEntity.status(401).body(Map.of("message", "error"));
    }

    // 401 → JWT invalide ou expiré
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex) {
        return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
    }

    // 400 / 404 → Runtime exception + gestion personnalisée
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {

        // 404 rental not found
        if ("RENTAL_NOT_FOUND".equals(ex.getMessage())) {
            return ResponseEntity.status(404).body(Map.of("message", "error"));
        }

        // 400 générique
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
