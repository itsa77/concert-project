package com.concertapp.dto;

public class LoginRequestDto {

    private String username;
    private String password; // plaintext password sent by user

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
