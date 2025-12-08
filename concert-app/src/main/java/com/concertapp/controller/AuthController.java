package com.concertapp.controller;

import com.concertapp.dto.LoginRequestDto;
import com.concertapp.dto.LoginResponseDto;
import com.concertapp.security.JwtUtil;
import com.concertapp.dao.UserDao;
import com.concertapp.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    public AuthController(UserDao userDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public User getCurrentUser(Authentication auth) {

        String username = auth.getName();
        return userDao.getUserByUsername(username);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userDao.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        System.out.println("PASSWORD SENT TO REGISTRATION: " + user.getPasswordHash());     //---------------
        String hashed = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt(12));
        user.setPasswordHash(hashed);
        User created = userDao.createUser(user);


        if (created == null) {
            return ResponseEntity.internalServerError().body("Error creating user");
        } return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userDao.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        System.out.println(
                "CHECK RESULT: " + BCrypt.checkpw(                                         //----------------------
                        loginRequest.getPassword(),
                        user.getPasswordHash()
                )
        );

        System.out.println("RAW PASSWORD: " + loginRequest.getPassword());
        System.out.println("HASH IN DB: " + user.getPasswordHash());

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        LoginResponseDto response = new LoginResponseDto(token, user.getUsername(), user.getUserId());
        return ResponseEntity.ok(response);
    }
}
