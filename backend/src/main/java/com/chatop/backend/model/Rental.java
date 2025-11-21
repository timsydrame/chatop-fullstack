package com.chatop.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double surface;
    private Double price;

    @Column(length = 1000)
    private String description;

    private String picture;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}

