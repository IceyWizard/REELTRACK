package com.reeltrack.repo;
import com.reeltrack.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByTmdbMovieIdOrderByCreatedAtDesc(Long movieId);
    Optional<Review> findByUserIdAndTmdbMovieId(Long userId, Long movieId);
}
