package com.reeltrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "library_movies", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tmdb_movie_id"}))
public class LibraryMovie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false) private Long userId;
    @Column(name = "tmdb_movie_id", nullable = false) private Long movieId;
    @Column(nullable = false, length = 300) private String title;
    @Column(length = 300) private String posterPath;
    @Column(length = 20) private String releaseDate;
    private Double voteAverage;
    @Column(nullable = false, length = 20) private String status;
    @Column(nullable = false) private LocalDateTime addedAt;

    protected LibraryMovie() {}
    public LibraryMovie(Long userId, Long movieId, String title, String posterPath, String releaseDate, Double voteAverage, String status) {
        this.userId=userId; this.movieId=movieId; this.title=title; this.posterPath=posterPath;
        this.releaseDate=releaseDate; this.voteAverage=voteAverage; this.status=status; this.addedAt=LocalDateTime.now();
    }
    public void update(String title, String posterPath, String releaseDate, Double voteAverage, String status) {
        if (title != null && !title.isBlank()) this.title=title;
        this.posterPath=posterPath; this.releaseDate=releaseDate; this.voteAverage=voteAverage; this.status=status;
    }
    public Long getId(){return id;} public Long getUserId(){return userId;} public Long getMovieId(){return movieId;}
    public String getTitle(){return title;} public String getPosterPath(){return posterPath;} public String getReleaseDate(){return releaseDate;}
    public Double getVoteAverage(){return voteAverage;} public String getStatus(){return status;} public LocalDateTime getAddedAt(){return addedAt;}
}
