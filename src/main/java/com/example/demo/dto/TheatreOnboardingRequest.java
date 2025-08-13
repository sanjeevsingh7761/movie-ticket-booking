package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalIdCache;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreOnboardingRequest {
    private String name;
    private String city;
    private String address;
}

