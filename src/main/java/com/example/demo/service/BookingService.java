package com.example.demo.service;

import com.example.demo.dto.TicketBookingRequest;
import com.example.demo.dto.TicketBookingResponse;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Show;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.ShowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private ShowRepository showRepo;
    @Autowired
    private SeatRepository seatRepo;
    @Autowired
    private BookingRepository bookingRepo;

    @Transactional
    public TicketBookingResponse bookTickets(TicketBookingRequest req) {
        Show show = showRepo.findById(req.showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Check seat availability
        List<Seat> seats = seatRepo.findByShowIdAndSeatNoInAndStatus(
                show.getId(), req.seatNumbers, Seat.SeatStatus.AVAILABLE);
        if(seats.size() != req.seatNumbers.size())
            throw new RuntimeException("Requested seats not available");

        // Apply discounts
        double totalPrice = seats.size() * show.getPrice();
        if(seats.size() >= 3)
            totalPrice -= show.getPrice() * 0.5; // 50% off 3rd ticket

        int hour = show.getShowTime().getHour();
        if(hour >= 12 && hour < 16)
            totalPrice *= 0.8; // 20% off for afternoon

        // Lock and update seat status
        seats.forEach(seat -> seat.setStatus(Seat.SeatStatus.BOOKED));
        seatRepo.saveAll(seats);

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(req.userId);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        booking.setBookedSeats(req.seatNumbers);
        booking.setStatus("SUCCESS");
        bookingRepo.save(booking);

        // Reduce available seats on show
        show.setAvailableSeats(show.getAvailableSeats() - seats.size());
        showRepo.save(show);

        // Payment simulated (integration point for gateways)
        // paymentService.charge(...)

        TicketBookingResponse resp = new TicketBookingResponse();
        resp.bookingId = booking.getId();
        resp.totalPrice = totalPrice;
        resp.status = "SUCCESS";
        resp.seatsBooked = req.seatNumbers;
        return resp;
    }
}

