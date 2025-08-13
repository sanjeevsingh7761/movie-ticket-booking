package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne
    private Show show;
    private LocalDateTime bookingTime;
    private double totalPrice;

    @ElementCollection
    private List<String> bookedSeats;
    private String status; // e.g., SUCCESS, FAILED
    // getters/setters
}

