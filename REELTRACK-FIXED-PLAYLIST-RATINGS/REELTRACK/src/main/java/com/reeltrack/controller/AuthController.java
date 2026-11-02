package com.reeltrack.controller;

import com.reeltrack.model.AppUser;
import com.reeltrack.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public AuthController(UserRepository users){this.users=users;}

    public record RegisterRequest(@NotBlank String username, @Email @NotBlank String email, @NotBlank String password) {}
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody RegisterRequest r, HttpSession session) {
        if(users.existsByUsername(r.username()) || users.existsByEmail(r.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Username or email already exists.");
        if(r.password().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password must be at least 8 characters.");
        AppUser u=users.save(new AppUser(r.username(),r.email(),encoder.encode(r.password())));
        session.setAttribute("userId",u.getId()); session.setAttribute("username",u.getUsername());
        return Map.of("id",u.getId(),"username",u.getUsername());
    }
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody LoginRequest r, HttpSession session) {
        AppUser u=users.findByUsername(r.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password."));
        if(!encoder.matches(r.password(),u.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password.");
        session.setAttribute("userId",u.getId()); session.setAttribute("username",u.getUsername());
        return Map.of("id",u.getId(),"username",u.getUsername());
    }
    @PostMapping("/logout") public Map<String,String> logout(HttpSession s){s.invalidate();return Map.of("message","Logged out");}
    @GetMapping("/me") public Map<String,Object> me(HttpSession s){
        Object id=s.getAttribute("userId"); if(id==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return Map.of("id",id,"username",s.getAttribute("username"));
    }
}
