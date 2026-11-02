package com.reeltrack.controller;

import com.reeltrack.model.LibraryMovie;
import com.reeltrack.repo.LibraryMovieRepository;
import com.reeltrack.repo.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
    private final ReviewRepository reviews;
    private final LibraryMovieRepository library;
    public LibraryController(ReviewRepository reviews, LibraryMovieRepository library){this.reviews=reviews;this.library=library;}
    private Long user(HttpSession s){Object id=s.getAttribute("userId");if(id==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please log in to manage your library.");return (Long)id;}

    @GetMapping("/watchlist") public List<LibraryMovie> watchlist(HttpSession s){return library.findByUserIdAndStatusOrderByAddedAtDesc(user(s),"WATCHLIST");}
    @GetMapping("/watched") public List<LibraryMovie> watched(HttpSession s){return library.findByUserIdAndStatusOrderByAddedAtDesc(user(s),"WATCHED");}

    @PostMapping("/{status}/{movieId}")
    public List<LibraryMovie> addMovie(@PathVariable String status, @PathVariable Long movieId, @RequestBody(required=false) MovieData data, HttpSession s){
        String target=normaliseStatus(status); Long userId=user(s);
        String title=data==null||data.title()==null||data.title().isBlank()?"Movie #"+movieId:data.title();
        var existing=library.findByUserIdAndMovieId(userId,movieId);
        if(existing.isPresent()){ existing.get().update(title,data==null?null:data.posterPath(),data==null?null:data.releaseDate(),data==null?null:data.voteAverage(),target); library.save(existing.get()); }
        else library.save(new LibraryMovie(userId,movieId,title,data==null?null:data.posterPath(),data==null?null:data.releaseDate(),data==null?null:data.voteAverage(),target));
        return library.findByUserIdAndStatusOrderByAddedAtDesc(userId,target);
    }
    @DeleteMapping("/{status}/{movieId}")
    public List<LibraryMovie> removeMovie(@PathVariable String status,@PathVariable Long movieId,HttpSession s){
        String target=normaliseStatus(status); Long userId=user(s);
        library.findByUserIdAndMovieId(userId,movieId).filter(m->m.getStatus().equals(target)).ifPresent(library::delete);
        return library.findByUserIdAndStatusOrderByAddedAtDesc(userId,target);
    }
    private String normaliseStatus(String status){
        if("watchlist".equalsIgnoreCase(status))return "WATCHLIST";
        if("watched".equalsIgnoreCase(status))return "WATCHED";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"List must be watchlist or watched.");
    }

    @PostMapping("/reviews") public Object review(@RequestBody ReviewBody b,HttpSession s){
        Long id=user(s); if(b.rating()<1||b.rating()>10)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rating must be 1–10.");
        var existing=reviews.findByUserIdAndTmdbMovieId(id,b.movieId());
        com.reeltrack.model.Review r;
        if(existing.isPresent()){r=existing.get();r.update(b.rating(),b.text());r=reviews.save(r);}
        else {r=reviews.save(new com.reeltrack.model.Review(id,b.movieId(),b.rating(),b.text()));}
        return Map.of("id",r.getId(),"movieId",r.getTmdbMovieId(),"rating",r.getRating(),"text",r.getReviewText()==null?"":r.getReviewText());
    }
    @GetMapping("/reviews/{movieId}") public Object reviews(@PathVariable Long movieId){return reviews.findByTmdbMovieIdOrderByCreatedAtDesc(movieId);}
    public record MovieData(String title,String posterPath,String releaseDate,Double voteAverage){}
    public record ReviewBody(Long movieId,int rating,String text){}
}
