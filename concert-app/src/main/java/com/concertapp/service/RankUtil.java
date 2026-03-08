package com.concertapp.service;

import com.concertapp.dto.UserRankDto;
public class RankUtil {

    public static UserRankDto fromTotalConcerts(int totalConcerts) {
        if (totalConcerts >= 100){
            return new UserRankDto(totalConcerts, "Spirit of Humungus", 7, null);
        } else if (totalConcerts >= 80) {
            return new UserRankDto(totalConcerts, "Phantom of the Pit", 6, 100);
        } else if (totalConcerts >= 60) {
            return new UserRankDto(totalConcerts, "Rail Warrior", 5, 80);
        } else if (totalConcerts >= 40) {
            return new UserRankDto(totalConcerts, "Setlist Sorcerer", 4, 60);
        } else if (totalConcerts >= 20) {
            return new UserRankDto(totalConcerts, "Merch Mage", 3, 40);
        } else if (totalConcerts >= 5) {
            return new UserRankDto(totalConcerts, "Pit Pilgrim", 2, 20);
        } else {
            return new UserRankDto(totalConcerts, "Greenie", 1, 5);
        }
    }
}
