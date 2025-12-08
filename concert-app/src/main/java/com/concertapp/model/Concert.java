package com.concertapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Concert {

    private int concertId;
    private int userId;
    private int artistId;
    private int venueId;
    private LocalDate date;
    private LocalDateTime createdAt;
    private Integer tourId;
    private Integer festivalId;
    private String notes;

    private List<Integer> openingActIds;

    public Concert() {}

    public Concert(int userId, int artistId, int venueId, LocalDate date) {
        this.userId = userId;
        this.artistId = artistId;
        this.venueId = venueId;
        this.date = date;
    }

    public Concert(int concertId, int userId, int artistId, int venueId,
                   LocalDate date, LocalDateTime createdAt, Integer tourId,
                   Integer festivalId, String notes, List<Integer> openingActIds) {
        this.concertId = concertId;
        this.userId = userId;
        this.artistId = artistId;
        this.venueId = venueId;
        this.date = date;
        this.createdAt = createdAt;
        this.tourId = tourId;
        this.festivalId = festivalId;
        this.notes = notes;
        this.openingActIds = openingActIds;
    }

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public Integer getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(Integer festivalId) {
        this.festivalId = festivalId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Integer> getOpeningActIds() {
        return openingActIds;
    }

    public void setOpeningActIds(List<Integer> openingActIds) {
        this.openingActIds = openingActIds;
    }
}
