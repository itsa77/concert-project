package com.concertapp.model;

public class Tour {

    private int tourId;
    private String name;

    public Tour() {}

    public Tour(int tourId, String name) {
        this.tourId = tourId;
        this.name = name;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
