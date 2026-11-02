package com.reeltrack.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Playlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long ownerId;
    private String title;
    @Column(length = 1000) private String description;
    private boolean isPublic;
    private String coverUrl;
    @ElementCollection
    @CollectionTable(name="playlist_movies", joinColumns=@JoinColumn(name="playlist_id"))
    @OrderColumn(name="movie_order")
    private List<Long> movieIds = new ArrayList<>();

    public Playlist() {}
    public Playlist(Long ownerId, String title, String description, boolean isPublic) {
        this.ownerId=ownerId; this.title=title; this.description=description; this.isPublic=isPublic;
    }
    public Long getId(){return id;} public Long getOwnerId(){return ownerId;}
    public String getTitle(){return title;} public String getDescription(){return description;}
    public boolean isPublic(){return isPublic;} public String getCoverUrl(){return coverUrl;}
    public List<Long> getMovieIds(){return movieIds;}
    public void setTitle(String v){title=v;} public void setDescription(String v){description=v;}
    public void setPublic(boolean v){isPublic=v;} public void setCoverUrl(String v){coverUrl=v;}
}
