package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNo;
    @ManyToOne
    private Show show;
    @Enumerated(EnumType.STRING)
    private SeatStatus status; // AVAILABLE, BOOKED
    // getters/setters

    public enum SeatStatus {
        AVAILABLE, BOOKED
    }
}

