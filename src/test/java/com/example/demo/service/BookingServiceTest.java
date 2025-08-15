package com.example.demo.service;

import com.example.demo.dto.TicketBookingRequest;
import com.example.demo.dto.TicketBookingResponse;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Show;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private ShowRepository showRepo;

    @Mock
    private SeatRepository seatRepo;

    @Mock
    private BookingRepository bookingRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookTickets_success() {
        // Prepare mock Show
        Show show = new Show();
        show.setId(1L);
        show.setPrice(200.0);
        show.setShowTime(LocalDateTime.of(2025, 8, 15, 14, 0));
        show.setAvailableSeats(100);

        // Prepare TicketBookingRequest
        TicketBookingRequest req = new TicketBookingRequest();
        req.showId = 1L;
        req.userId = 10L;
        req.seatNumbers = List.of("A1", "A2", "A3");

        // Prepare mock Seats
        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setSeatNo("A1");
        seat1.setStatus(Seat.SeatStatus.AVAILABLE);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNo("A2");
        seat2.setStatus(Seat.SeatStatus.AVAILABLE);

        Seat seat3 = new Seat();
        seat3.setId(3L);
        seat3.setSeatNo("A3");
        seat3.setStatus(Seat.SeatStatus.AVAILABLE);

        List<Seat> availableSeats = List.of(seat1, seat2, seat3);

        // Mocking repo behaviors
        when(showRepo.findById(1L)).thenReturn(Optional.of(show));
        when(seatRepo.findByShowIdAndSeatNoInAndStatus(1L, req.seatNumbers, Seat.SeatStatus.AVAILABLE))
                .thenReturn(availableSeats);
        when(bookingRepo.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(100L);
            return b;
        });
        when(seatRepo.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(showRepo.save(any(Show.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call method under test
        TicketBookingResponse resp = bookingService.bookTickets(req);

        // Assertions
        assertNotNull(resp);
        assertEquals(100L, resp.bookingId);
        assertEquals("SUCCESS", resp.status);
        assertEquals(3, resp.seatsBooked.size());

        // Calculate expected price:
        // 3 seats x 200 = 600 - 50% off on 3rd ticket (i.e. 100 off) = 500
        // Afternoon show discount 20% off on 500 = 400
        assertEquals(400.0, resp.totalPrice);

        // Verify interactions
        verify(seatRepo, times(1)).saveAll(anyList());
        verify(bookingRepo, times(1)).save(any(Booking.class));
        verify(showRepo, times(1)).save(any(Show.class));
    }

    @Test
    public void testBookTickets_showNotFound() {
        when(showRepo.findById(anyLong())).thenReturn(Optional.empty());

        TicketBookingRequest req = new TicketBookingRequest();
        req.showId = 1L;

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.bookTickets(req));
        assertEquals("Show not found", ex.getMessage());
    }

    @Test
    public void testBookTickets_seatsNotAvailable() {
        Show show = new Show();
        show.setId(1L);

        when(showRepo.findById(1L)).thenReturn(Optional.of(show));
        when(seatRepo.findByShowIdAndSeatNoInAndStatus(anyLong(), anyList(), any()))
                .thenReturn(List.of()); // No available seats

        TicketBookingRequest req = new TicketBookingRequest();
        req.showId = 1L;
        req.seatNumbers = List.of("A1", "A2");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.bookTickets(req));
        assertEquals("Requested seats not available", ex.getMessage());
    }
}
