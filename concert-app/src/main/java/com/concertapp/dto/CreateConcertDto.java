package com.concertapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CreateConcertDto {

    private String artistName;
    private int venueId;
    private LocalDate date;
    private LocalTime startTime;

    private List<String> openingActNames;
    private String tourName;
    private String festivalName;

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }


    public List<String> getOpeningActNames() { return openingActNames; }
    public void setOpeningActNames(List<String> openingActNames) { this.openingActNames = openingActNames; }

    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }

    public String getFestivalName() { return festivalName; }
    public void setFestivalName(String festivalName) { this.festivalName = festivalName; }

}


