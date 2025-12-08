package com.concertapp.model;

public class UserYearlyStats {

    private int userId;
    private int year;
    private int concertCount;

    public UserYearlyStats() {}

    public UserYearlyStats(int userId, int year, int concertCount) {
        this.userId = userId;
        this.year = year;
        this.concertCount = concertCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getConcertCount() {
        return concertCount;
    }

    public void setConcertCount(int concertCount) {
        this.concertCount = concertCount;
    }
}
