package com.concertapp.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Concert {

    private int concertId;
    private int artistId;
    private int venueId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer tourId;
    private Integer festivalId;
    private int createdBy;
    private LocalDateTime createdAt;

    private List<Integer> openingActIds;

    public Concert() {}

    public Concert(int concertId, int artistId, int venueId, LocalDate date,
                   LocalTime startTime, Integer tourId, Integer festivalId,
                   int createdBy, LocalDateTime createdAt, List<Integer> openingActIds) {
        this.concertId = concertId;
        this.artistId = artistId;
        this.venueId = venueId;
        this.date = date;
        this.startTime = startTime;
        this.tourId = tourId;
        this.festivalId = festivalId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.openingActIds = openingActIds;
    }

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
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


    public List<Integer> getOpeningActIds() {
        return openingActIds;
    }

    public void setOpeningActIds(List<Integer> openingActIds) {
        this.openingActIds = openingActIds;
    }


}
