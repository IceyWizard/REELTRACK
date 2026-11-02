package com.reeltrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"userId","tmdbMovieId"}))
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long userId;
    private Long tmdbMovieId;
    private int rating;
    @Column(length=3000) private String reviewText;
    private LocalDateTime createdAt = LocalDateTime.now();
    public Review() {}
    public Review(Long userId, Long movieId, int rating, String text) {
        this.userId=userId; this.tmdbMovieId=movieId; this.rating=rating; this.reviewText=text;
    }
    public void update(int rating, String text) { this.rating=rating; this.reviewText=text; this.createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public Long getUserId(){return userId;}
    public Long getTmdbMovieId(){return tmdbMovieId;} public int getRating(){return rating;}
    public String getReviewText(){return reviewText;} public LocalDateTime getCreatedAt(){return createdAt;}
}
