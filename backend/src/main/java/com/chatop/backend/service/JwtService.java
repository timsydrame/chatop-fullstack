package com.chatop.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.nimbusds.jwt.JWTClaimNames.EXPIRATION_TIME;

@Service
public class JwtService {
    public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final String SECRET_KEY = "1234567890AZERTYUIOPQSDFGHJKLMWXCVBN1234567890AZERTYUIOPQSDFGHJKLMWXCVBN"; // clé secrète (à changer plus tard)
    private static final long EXPIRATION_TIME = 3600_000; // 1h
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                    // utilisateur
                .setIssuedAt(new Date())                 // date d’émission
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // date d’expiration
                .signWith(key)                           // signature
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
