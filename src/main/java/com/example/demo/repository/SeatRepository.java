package com.example.demo.repository;

import com.example.demo.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowIdAndStatus(Long showId, Seat.SeatStatus status);
    List<Seat> findByShowIdAndSeatNoInAndStatus(Long showId, List<String> seatNos, Seat.SeatStatus status);
}
