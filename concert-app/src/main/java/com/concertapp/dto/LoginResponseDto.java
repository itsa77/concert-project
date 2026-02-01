package com.concertapp.dto;
import java.util.List;

public class LoginResponseDto {

    private String token;
    private String username;
    private int userId;
    private List<String> roles;

    public LoginResponseDto(String token, String username, int userId, List<String> roles) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public List<String> getRoles() {
        return roles;
    }
}
