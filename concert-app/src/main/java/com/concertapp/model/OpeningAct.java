package com.concertapp.model;

public class OpeningAct {

    private int concertId;
    private int artistId;

    public OpeningAct() {}

    public OpeningAct(int concertId, int artistId) {
        this.concertId = concertId;
        this.artistId = artistId;
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
}
