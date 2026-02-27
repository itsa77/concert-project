package com.concertapp.dto;

public class MonthStatsDto {
    private int month;
    private int concertCount;

    public MonthStatsDto() {}

    public MonthStatsDto(int month ,int concertCount) {
        this.month = month;
        this.concertCount = concertCount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getConcertCount() {
        return concertCount;
    }

    public void setConcertCount(int concertCount) {
        this.concertCount = concertCount;
    }
}
