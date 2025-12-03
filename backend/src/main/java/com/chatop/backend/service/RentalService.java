package com.chatop.backend.service;

import com.chatop.backend.dto.RentalRequest;
import com.chatop.backend.dto.RentalResponse;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // ------------------------------------------
    // 1) GET ALL
    // ------------------------------------------
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------------------------------
    // 2) GET BY ID
    // ------------------------------------------
    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RENTAL_NOT_FOUND"));
        return mapToResponse(rental);
    }

    // ------------------------------------------
    // 3) CREATE RENTAL + UPLOAD
    // ------------------------------------------
    public RentalResponse createRental(
            RentalRequest rentalRequest, String email
    ) throws IOException {
        User ownerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        // dossier uploads/
        String uploadDir = "uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        // nom unique du fichier
        String uniqueName = UUID.randomUUID() + "_" + rentalRequest.getPicture().getOriginalFilename();
        Path picturePath = Paths.get(uploadDir + uniqueName);

        Files.copy(rentalRequest.getPicture().getInputStream(), picturePath, StandardCopyOption.REPLACE_EXISTING);

        Rental rental = Rental.builder()
                .name(rentalRequest.getName())
                .surface(rentalRequest.getSurface())
                .price(rentalRequest.getPrice())
                .description(rentalRequest.getDescription())
                .picture(uniqueName)
                .owner(ownerUser)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        rentalRepository.save(rental);

        return mapToResponse(rental);
    }

    public RentalResponse updateRental(Long id, RentalRequest rentalRequest, String email) throws IOException {

        User ownerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RENTAL_NOT_FOUND"));

        // Vérifier que l’utilisateur connecté est bien le propriétaire
        if (!rental.getOwner().getId().equals(ownerUser.getId())) {
            throw new RuntimeException("NOT_ALLOWED");
        }

        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setDescription(rentalRequest.getDescription());

        // Upload d’une nouvelle image si elle existe
        if (rentalRequest.getPicture() != null && !rentalRequest.getPicture().isEmpty()) {

            String uploadDir = "uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();

            String uniqueName = UUID.randomUUID() + "_" +
                    rentalRequest.getPicture().getOriginalFilename();

            Path picturePath = Paths.get(uploadDir + uniqueName);

            Files.copy(rentalRequest.getPicture().getInputStream(),
                    picturePath,
                    StandardCopyOption.REPLACE_EXISTING);

            rental.setPicture(uniqueName);
        }

        rental.setUpdatedAt(LocalDate.now());

        rentalRepository.save(rental);

        return mapToResponse(rental);
    }


    // ------------------------------------------
    // 4) mapToResponse
    // ------------------------------------------
    private RentalResponse mapToResponse(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                "http://localhost:8080/uploads/" + rental.getPicture(), //  URL complète
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt().format(FORMATTER),
                rental.getUpdatedAt().format(FORMATTER)
        );
    }


}
