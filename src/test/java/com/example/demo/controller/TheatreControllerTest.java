package com.example.demo.controller;

import com.example.demo.dto.TheatreOnboardingRequest;
import com.example.demo.entity.Theatre;
import com.example.demo.service.TheatreService;
import com.example.demo.service.TheatreServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TheatreControllerTest {

    @InjectMocks
    private TheatreController theatreController;

    @Mock
    private TheatreService theatreService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(theatreController).build();
    }

    @Test
    public void testOnboardTheatre_success() throws Exception {
        Theatre savedTheatre = new Theatre();
        savedTheatre.setId(1L);
        savedTheatre.setName("Test Theatre");
        savedTheatre.setCity("Test City");
        savedTheatre.setAddress("Test Address");

        when(theatreService.onboardTheatre(any(TheatreOnboardingRequest.class)))
                .thenReturn(savedTheatre);

        String requestBody = """
            {
                "name": "Test Theatre",
                "city": "Test City",
                "address": "Test Address"
            }
        """;

        mockMvc.perform(post("/api/theatres/onboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Theatre"))
                .andExpect(jsonPath("$.city").value("Test City"))
                .andExpect(jsonPath("$.address").value("Test Address"));

        verify(theatreService, times(1)).onboardTheatre(any(TheatreOnboardingRequest.class));
    }
}

