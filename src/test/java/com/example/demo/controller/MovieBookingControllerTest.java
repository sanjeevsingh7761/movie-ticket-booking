package com.example.demo.controller;

import com.example.demo.dto.TicketBookingRequest;
import com.example.demo.dto.TicketBookingResponse;
import com.example.demo.dto.TheatreShowResponse;
import com.example.demo.service.BookingService;
import com.example.demo.service.BookingServiceTest;
import com.example.demo.service.ShowService;
import com.example.demo.service.ShowServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

public class MovieBookingControllerTest {

    @InjectMocks
    private MovieBookingController controller;

    @Mock
    private ShowService showService;

    @Mock
    private BookingService bookingService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetTheatres() throws Exception {
        TheatreShowResponse.ShowDetails showDetails = new TheatreShowResponse.ShowDetails();
        showDetails.setShowId(1L);
        showDetails.setShowTime(LocalDateTime.of(2025,8,15,10,30));
        showDetails.setPrice(250.0);
        showDetails.setAvailableSeats(100);

        TheatreShowResponse response = new TheatreShowResponse();
        response.setTheatreName("Test Theatre");
        response.setAddress("Test Address");
        response.setShows(Collections.singletonList(showDetails));

        when(showService.getTheatresByMovieCityDate(anyLong(), anyString(), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/movies/1/theatres")
                        .param("city", "TestCity")
                        .param("date", "2025-08-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].theatreName").value("Test Theatre"))
                .andExpect(jsonPath("$[0].shows[0].showId").value(1));

        verify(showService, times(1))
                .getTheatresByMovieCityDate(eq(1L), eq("TestCity"), eq(LocalDate.of(2025,8,15)));
    }

    @Test
    void testBookTicket() throws Exception {
        TicketBookingRequest request = new TicketBookingRequest();
        request.showId = 1L;
        request.userId = 100L;
        request.seatNumbers = List.of("A1", "A2", "A3");

        TicketBookingResponse response = new TicketBookingResponse();
        response.bookingId = 10L;
        response.totalPrice = 500.0;
        response.status = "SUCCESS";
        response.seatsBooked = request.seatNumbers;

        when(bookingService.bookTickets(any(TicketBookingRequest.class))).thenReturn(response);

        String jsonRequest = """
            {
                "showId": 1,
                "userId": 100,
                "seatNumbers": ["A1", "A2", "A3"]
            }
            """;

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.bookingId").value(10))
                .andExpect(jsonPath("$.totalPrice").value(500.0));

        verify(bookingService, times(1)).bookTickets(any(TicketBookingRequest.class));
    }
}
