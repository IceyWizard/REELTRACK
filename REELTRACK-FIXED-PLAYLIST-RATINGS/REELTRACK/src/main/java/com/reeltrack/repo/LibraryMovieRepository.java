package com.reeltrack.repo;

import com.reeltrack.model.LibraryMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibraryMovieRepository extends JpaRepository<LibraryMovie, Long> {
    List<LibraryMovie> findByUserIdAndStatusOrderByAddedAtDesc(Long userId, String status);
    Optional<LibraryMovie> findByUserIdAndMovieId(Long userId, Long movieId);
}
