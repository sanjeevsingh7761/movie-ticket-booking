package com.example.demo.controller;

import com.example.demo.dto.TheatreShowResponse;
import com.example.demo.dto.TicketBookingRequest;
import com.example.demo.dto.TicketBookingResponse;
import com.example.demo.service.BookingService;
import com.example.demo.service.ShowService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieBookingController {
    @Autowired
    private ShowService showService;
    @Autowired
    private BookingService bookingService;

    // READ: Browse
    @GetMapping("/movies/{movieId}/theatres")
    public List<TheatreShowResponse> getTheatres(
            @PathVariable Long movieId,
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return showService.getTheatresByMovieCityDate(movieId, city, date);
    }

    // WRITE: Book
    @PostMapping("/bookings")
    public TicketBookingResponse bookTicket(@RequestBody TicketBookingRequest req) {
        return bookingService.bookTickets(req);
    }
}

