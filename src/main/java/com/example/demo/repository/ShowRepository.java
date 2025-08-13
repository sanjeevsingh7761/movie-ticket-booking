package com.example.demo.repository;

import com.example.demo.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieIdAndTheatre_CityAndShowTimeBetween(
            Long movieId, String city, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
