package com.concertapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DayStatsDto {

    private int concertId;
    private LocalDate date;
    private LocalTime startTime;
    private String artistName;
    private String venueName;
    private String venueCity;
    private String venueState;
    private String tourName;
    private String festivalName;
    private List<String> openingActNames;

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public String getVenueState() {
        return venueState;
    }

    public void setVenueState(String venueState) {
        this.venueState = venueState;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public List<String> getOpeningActNames() {
        return openingActNames;
    }

    public void setOpeningActNames(List<String> openingActNames) {
        this.openingActNames = openingActNames;
    }
}
