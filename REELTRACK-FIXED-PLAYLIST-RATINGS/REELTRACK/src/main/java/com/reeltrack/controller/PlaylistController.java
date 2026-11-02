package com.reeltrack.controller;

import com.reeltrack.model.Playlist;
import com.reeltrack.repo.PlaylistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistRepository repo;
    public PlaylistController(PlaylistRepository repo){this.repo=repo;}
    private Long user(HttpSession s){Object id=s.getAttribute("userId");if(id==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please log in.");return (Long)id;}
    @GetMapping public List<Playlist> mine(HttpSession s){return repo.findByOwnerId(user(s));}
    @PostMapping public Playlist create(@RequestBody CreateBody b,HttpSession s){return repo.save(new Playlist(user(s),b.title(),b.description(),b.isPublic()));}
    @GetMapping("/public") public List<Playlist> publicPlaylists(){return repo.findByIsPublicTrue();}
    @GetMapping("/public/{id}") public Playlist publicOne(@PathVariable Long id){return repo.findByIdAndIsPublicTrue(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
    @PostMapping("/{id}/movies/{movieId}") public Playlist add(@PathVariable Long id,@PathVariable Long movieId,HttpSession s){
        Playlist p=owned(id,s);if(!p.getMovieIds().contains(movieId))p.getMovieIds().add(movieId);return repo.save(p);
    }
    @DeleteMapping("/{id}/movies/{movieId}") public Playlist remove(@PathVariable Long id,@PathVariable Long movieId,HttpSession s){
        Playlist p=owned(id,s);p.getMovieIds().remove(movieId);return repo.save(p);
    }
    @PutMapping("/{id}/movies") public Playlist reorder(@PathVariable Long id,@RequestBody List<Long> ids,HttpSession s){
        Playlist p=owned(id,s);if(!new HashSet<>(p.getMovieIds()).equals(new HashSet<>(ids)))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Reorder must contain the same movie IDs.");p.getMovieIds().clear();p.getMovieIds().addAll(ids);return repo.save(p);
    }
    @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id,HttpSession s){repo.delete(owned(id,s));return Map.of("message","Deleted");}
    private Playlist owned(Long id,HttpSession s){Playlist p=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));if(!p.getOwnerId().equals(user(s)))throw new ResponseStatusException(HttpStatus.FORBIDDEN);return p;}
    public record CreateBody(String title,String description,boolean isPublic){}
}
