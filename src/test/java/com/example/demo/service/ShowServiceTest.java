package com.example.demo.service;

import com.example.demo.dto.TheatreShowResponse;
import com.example.demo.entity.Show;
import com.example.demo.entity.Theatre;
import com.example.demo.repository.ShowRepository;
import com.example.demo.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ShowServiceTest {

    @InjectMocks
    private ShowService showService;

    @Mock
    private ShowRepository showRepo;

    @Mock
    private TheatreRepository theatreRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTheatresByMovieCityDate() {
        Long movieId = 1L;
        String city = "TestCity";
        LocalDate date = LocalDate.of(2025, 8, 15);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59,59);

        Theatre theatre1 = new Theatre();
        theatre1.setId(10L);
        theatre1.setName("Theatre One");
        theatre1.setCity(city);
        theatre1.setAddress("Address One");

        Show show1 = new Show();
        show1.setId(100L);
        show1.setMovie(null); // Not needed for this test
        show1.setTheatre(theatre1);
        show1.setShowTime(LocalDateTime.of(2025,8,15,10,0));
        show1.setPrice(150.0);
        show1.setAvailableSeats(50);

        Mockito.when(showRepo.findByMovieIdAndTheatre_CityAndShowTimeBetween(
                        eq(movieId), eq(city), eq(start), eq(end)))
                .thenReturn(List.of(show1));

        List<TheatreShowResponse> result = showService.getTheatresByMovieCityDate(movieId, city, date);

        assertNotNull(result);
        assertEquals(1, result.size());

        TheatreShowResponse theatreResp = result.get(0);
        assertEquals("Theatre One", theatreResp.getTheatreName());
        assertEquals("Address One", theatreResp.getAddress());
        assertEquals(1, theatreResp.getShows().size());

        TheatreShowResponse.ShowDetails showDetails = theatreResp.getShows().get(0);
        assertEquals(100L, showDetails.showId);
        assertEquals(LocalDateTime.of(2025,8,15,10,0), showDetails.showTime);
        assertEquals(150.0, showDetails.price);
        assertEquals(50, showDetails.availableSeats);

        verify(showRepo, times(1)).findByMovieIdAndTheatre_CityAndShowTimeBetween(movieId, city, start, end);
    }
}
