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

        public Long getShowId() {
            return showId;
        }

        public void setShowId(Long showId) {
            this.showId = showId;
        }

        public LocalDateTime getShowTime() {
            return showTime;
        }

        public void setShowTime(LocalDateTime showTime) {
            this.showTime = showTime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getAvailableSeats() {
            return availableSeats;
        }

        public void setAvailableSeats(int availableSeats) {
            this.availableSeats = availableSeats;
        }

        public LocalDateTime showTime;
        public double price;
        public int availableSeats;
    }
}

