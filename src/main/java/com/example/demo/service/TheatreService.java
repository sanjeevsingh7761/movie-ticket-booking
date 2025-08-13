package com.example.demo.service;

import com.example.demo.dto.TheatreOnboardingRequest;
import com.example.demo.entity.Theatre;
import com.example.demo.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public Theatre onboardTheatre(TheatreOnboardingRequest request) {
        Theatre theatre = new Theatre();
        theatre.setName(request.getName());
        theatre.setCity(request.getCity());
        theatre.setAddress(request.getAddress());
        return theatreRepository.save(theatre);
    }
}
