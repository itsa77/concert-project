package com.concertapp.model;

public class Venue {

    private int venueId;
    private String name;
    private String state;
    private String city;

    public Venue() {}

    public Venue(int venueId, String name, String state, String city) {
        this.venueId = venueId;
        this.name = name;
        this.state = state;
        this.city = city;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
