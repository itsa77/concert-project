package com.concertapp.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateConcertDto {

    private String artistName;              // main artist (instead of ID)
    private int venueId;
    private LocalDate date;
    private String notes;

    private List<String> openingActNames;   // opening act names instead of IDs
    private String tourName;
    private String festivalName;

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<String> getOpeningActNames() { return openingActNames; }
    public void setOpeningActNames(List<String> openingActNames) { this.openingActNames = openingActNames; }

    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }

    public String getFestivalName() { return festivalName; }
    public void setFestivalName(String festivalName) { this.festivalName = festivalName; }
}

