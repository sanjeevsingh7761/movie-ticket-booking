package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheatreShowResponse {
    private String theatreName;
    private String address;
    private List<ShowDetails> shows;

    public static class ShowDetails {
        public Long showId;
        public LocalDateTime showTime;
        public double price;
        public int availableSeats;
    }
}

