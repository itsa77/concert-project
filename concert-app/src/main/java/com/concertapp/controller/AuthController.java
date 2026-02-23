package com.concertapp.controller;

import com.concertapp.dto.LoginRequestDto;
import com.concertapp.dao.UserDao;
import com.concertapp.model.User;
import com.concertapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final UserDao userDao; // only needed if you keep /me returning user directly

    public AuthController(AuthService authService, UserDao userDao) {
        this.authService = authService;
        this.userDao = userDao;
    }

    @GetMapping("/me")
    public User getCurrentUser(Authentication auth) {
        return authService.getCurrentUser(auth.getName());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            authService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            // quick/simple mapping; later you can replace with @ControllerAdvice
            if ("Username already exists".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            return ResponseEntity.ok(authService.login(loginRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}
