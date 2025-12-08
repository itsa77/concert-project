package com.concertapp.model;

public class Festival {

    private int festivalId;
    private String name;

    public Festival() {}

    public Festival(int festivalId, String name) {

        this.festivalId = festivalId;
        this.name = name;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
