package com.chatop.backend.service;

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
            String name,
            Double surface,
            Double price,
            String description,
            MultipartFile picture,
            Long ownerId
    ) throws IOException {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        // dossier uploads/
        String uploadDir = "uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        // nom unique du fichier
        String uniqueName = UUID.randomUUID() + "_" + picture.getOriginalFilename();
        Path picturePath = Paths.get(uploadDir + uniqueName);

        Files.copy(picture.getInputStream(), picturePath, StandardCopyOption.REPLACE_EXISTING);

        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                .picture(uniqueName)
                .owner(owner)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

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
                "http://localhost:8080/uploads/" + rental.getPicture(), // 👍 URL complète
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt().format(FORMATTER),
                rental.getUpdatedAt().format(FORMATTER)
        );
    }
}
