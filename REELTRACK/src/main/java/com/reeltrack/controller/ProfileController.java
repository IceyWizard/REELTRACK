package com.reeltrack.controller;
import com.reeltrack.repo.*; import jakarta.servlet.http.HttpSession; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.*;
@RestController @RequestMapping("/api/profile") public class ProfileController {
 private final UserRepository users; private final LibraryMovieRepository library; private final ReviewRepository reviews; private final PlaylistRepository playlists;
 public ProfileController(UserRepository u,LibraryMovieRepository l,ReviewRepository r,PlaylistRepository p){users=u;library=l;reviews=r;playlists=p;}
 @GetMapping public Object profile(HttpSession s){Object raw=s.getAttribute("userId");if(raw==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please log in.");Long id=(Long)raw;var u=users.findById(id).orElseThrow();var wl=library.findByUserIdAndStatusOrderByAddedAtDesc(id,"WATCHLIST");var seen=library.findByUserIdAndStatusOrderByAddedAtDesc(id,"WATCHED");var lists=playlists.findByOwnerId(id);return Map.of("username",u.getUsername(),"email",u.getEmail(),"watchlistCount",wl.size(),"watchedCount",seen.size(),"playlistCount",lists.size(),"reviewCount",reviews.findAll().stream().filter(x->x.getUserId().equals(id)).count());}
}
