package com.concertapp.dao;

import com.concertapp.model.Venue;

import java.util.List;

public interface VenueDao {
    Integer getOrCreateVenueId(String venueName, String city, String state);

    Venue getVenueById(int venueId);

    List<Venue> getAllVenues();
}
