package com.concertapp.dto;

public class LoginResponseDto {

    private String token;
    private String username;
    private int userId;

    public LoginResponseDto(String token, String username, int userId) {
        this.token = token;
        this.username = username;
        this.userId = userId;
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
}
