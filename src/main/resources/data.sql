-- Insert movies
INSERT INTO movie (id, name, genre, language, duration) VALUES (100, 'Sample Movie', 'Action', 'English', 120);

-- Insert theatres
INSERT INTO theatre (id, name, city, address) VALUES (10, 'PVR Koramangala', 'Bangalore', 'Koramangala 7th Block');

-- Insert shows
INSERT INTO movie_show (id, movie_id, theatre_id, show_time, price, total_seats, available_seats)
VALUES (2002, 100, 10, '2025-08-15 14:00:00', 250, 150, 150);

-- Insert seats for the show (example)
INSERT INTO seat (seat_no, show_id, status) VALUES
('A1', 2002, 'AVAILABLE'),
('A2', 2002, 'AVAILABLE'),
('A3', 2002, 'AVAILABLE'),
('A4', 2002, 'AVAILABLE');