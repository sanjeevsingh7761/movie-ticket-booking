package com.example.demo.service;

import com.example.demo.dto.TheatreOnboardingRequest;
import com.example.demo.entity.Theatre;
import com.example.demo.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TheatreServiceTest {

    @InjectMocks
    private TheatreService theatreService;

    @Mock
    private TheatreRepository theatreRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnboardTheatre() {
        TheatreOnboardingRequest request = new TheatreOnboardingRequest();
        request.setName("New Theatre");
        request.setCity("New City");
        request.setAddress("New Address");

        Theatre savedTheatre = new Theatre();
        savedTheatre.setId(1L);
        savedTheatre.setName("New Theatre");
        savedTheatre.setCity("New City");
        savedTheatre.setAddress("New Address");

        when(theatreRepository.save(any(Theatre.class))).thenReturn(savedTheatre);

        Theatre result = theatreService.onboardTheatre(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Theatre", result.getName());
        assertEquals("New City", result.getCity());
        assertEquals("New Address", result.getAddress());

        verify(theatreRepository, times(1)).save(any(Theatre.class));
    }
}
