package com.example.demo.service;

import com.example.demo.dto.TheatreShowResponse;
import com.example.demo.entity.Show;
import com.example.demo.entity.Theatre;
import com.example.demo.repository.ShowRepository;
import com.example.demo.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepo;
    @Autowired
    private TheatreRepository theatreRepo;

    public List<TheatreShowResponse> getTheatresByMovieCityDate(Long movieId, String city, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23,59,59);

        List<Show> shows = showRepo.findByMovieIdAndTheatre_CityAndShowTimeBetween(movieId, city, start, end);

        Map<Theatre, List<Show>> theatreShowMap = shows.stream()
                .collect(Collectors.groupingBy(Show::getTheatre));

        List<TheatreShowResponse> result = new ArrayList<>();
        for(Map.Entry<Theatre, List<Show>> entry : theatreShowMap.entrySet()) {
            Theatre theatre = entry.getKey();
            List<Show> showList = entry.getValue();

            TheatreShowResponse resp = new TheatreShowResponse();
            resp.setTheatreName(theatre.getName());
            resp.setAddress(theatre.getAddress());
            resp.setShows(showList.stream().map(show -> {
                TheatreShowResponse.ShowDetails sd = new TheatreShowResponse.ShowDetails();
                sd.showId = show.getId();
                sd.showTime = show.getShowTime();
                sd.price = show.getPrice();
                sd.availableSeats = show.getAvailableSeats();
                return sd;
            }).collect(Collectors.toList()));
            result.add(resp);
        }
        return result;
    }
}
