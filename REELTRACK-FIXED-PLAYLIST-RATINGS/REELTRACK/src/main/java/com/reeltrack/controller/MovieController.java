package com.reeltrack.controller;
import com.reeltrack.service.TmdbService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/movies")
public class MovieController {
 private final TmdbService tmdb; public MovieController(TmdbService tmdb){this.tmdb=tmdb;}
 @GetMapping("/trending") public Object trending(){return tmdb.get("/trending/movie/week",Map.of());}
 @GetMapping("/popular") public Object popular(){return tmdb.get("/movie/popular",Map.of());}
 @GetMapping("/now-playing") public Object nowPlaying(){return tmdb.get("/movie/now_playing",Map.of("region","IN"));}
 @GetMapping("/top-rated") public Object topRated(){return tmdb.get("/movie/top_rated",Map.of());}
 @GetMapping("/search") public Object search(@RequestParam String query){return tmdb.get("/search/movie",Map.of("query",query,"include_adult","false"));}
 @GetMapping("/genres") public Object genres(){return tmdb.get("/genre/movie/list",Map.of());}
 @GetMapping("/discover") public Object discover(@RequestParam(required=false) Integer genre,@RequestParam(required=false) Integer year,@RequestParam(defaultValue="popularity.desc") String sort,@RequestParam(defaultValue="1") Integer page){Map<String,String> p=new HashMap<>();p.put("sort_by",sort);p.put("page",String.valueOf(Math.max(1,Math.min(page,10))));if(genre!=null)p.put("with_genres",genre.toString());if(year!=null)p.put("primary_release_year",year.toString());return tmdb.get("/discover/movie",p);}
 @GetMapping("/{id}") public Object details(@PathVariable Long id){return tmdb.get("/movie/"+id,Map.of("append_to_response","credits,videos,watch/providers,recommendations"));}
}
