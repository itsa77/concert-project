package com.concertapp.model;

import java.time.LocalDateTime;

public class User {

    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private int totalConcerts;
    private boolean wantingEmailUpdates;
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String firstName, String lastName,
                String email, String passwordHash, int totalConcerts, boolean wantingEmailUpdates) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.totalConcerts = 0;
        this.wantingEmailUpdates = true;

    }

    //   public User(String username, String firstName, String lastName, String email, String passwordHash) {
    //   }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getTotalConcerts() {
        return totalConcerts;
    }

    public void setTotalConcerts(int totalConcerts) {
        this.totalConcerts = totalConcerts;
    }

    public boolean isWantingEmailUpdates() {
        return wantingEmailUpdates;
    }

    public void setWantingEmailUpdates(boolean wantingEmailUpdates) {
        this.wantingEmailUpdates = wantingEmailUpdates;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
