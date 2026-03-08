package com.concertapp.dto;

public class UserRankDto {

    private int totalConcerts;
    private String rankName;
    private int rankLevel;
    private Integer nextThreshold;

    public UserRankDto() {}

    public UserRankDto(int totalConcerts, String rankName, int rankLevel, Integer nextThreshold) {
        this.totalConcerts = totalConcerts;
        this.rankName = rankName;
        this.rankLevel = rankLevel;
        this.nextThreshold = nextThreshold;
    }

    public int getTotalConcerts() {
        return totalConcerts;
    }

    public void setTotalConcerts(int totalConcerts) {
        this.totalConcerts = totalConcerts;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public int getRankLevel() {
        return rankLevel;
    }

    public void setRankLevel(int rankLevel) {
        this.rankLevel = rankLevel;
    }

    public Integer getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(Integer nextThreshold) {
        this.nextThreshold = nextThreshold;
    }
}
