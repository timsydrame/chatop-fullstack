package com.chatop.backend.service;

import com.chatop.backend.dto.RentalRequest;
import com.chatop.backend.dto.RentalResponse;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RENTAL_NOT_FOUND"));
        return mapToResponse(rental);
    }

    public RentalResponse createRental(RentalRequest request, Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        LocalDateTime now = LocalDateTime.now();

        Rental rental = Rental.builder()
                .name(request.getName())
                .surface(request.getSurface())
                .price(request.getPrice())
                .description(request.getDescription())
                .picture(request.getPicture())
                .owner(owner)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        rentalRepository.save(rental);

        return mapToResponse(rental);
    }

    private RentalResponse mapToResponse(Rental rental) {
        String createdAt = rental.getCreatedAt() != null
                ? rental.getCreatedAt().format(FORMATTER)
                : "2022/02/02";
        String updatedAt = rental.getUpdatedAt() != null
                ? rental.getUpdatedAt().format(FORMATTER)
                : "2022/08/02";

        return new RentalResponse(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                createdAt,
                updatedAt
        );
    }
}
