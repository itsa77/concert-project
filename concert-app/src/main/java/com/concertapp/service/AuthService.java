package com.concertapp.service;

import com.concertapp.dao.UserDao;
import com.concertapp.dto.LoginRequestDto;
import com.concertapp.dto.LoginResponseDto;
import com.concertapp.model.User;
import com.concertapp.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    public AuthService(UserDao userDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public User getCurrentUser(String username) {
        return userDao.getUserByUsername(username);
    }

    @Transactional
    public void register(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        // whatever field you are currently using to carry the raw password:
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (userDao.getUserByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        String hashed = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt(12));
        user.setPasswordHash(hashed);

        User created = userDao.createUser(user);
        if (created == null) {
            throw new RuntimeException("Error creating user");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequest) {

        User user = userDao.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        List<String> roles = userDao.getUserRolesByUsername(user.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), roles);

        return new LoginResponseDto(token, user.getUsername(),user.getUserId(),roles);
    }


}

