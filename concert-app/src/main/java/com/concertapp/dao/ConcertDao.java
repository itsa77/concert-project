package com.concertapp.dao;

import com.concertapp.model.Concert;

import java.util.List;

public interface ConcertDao {
    Concert createConcert(Concert concert, String tourName, String festivalName);

    boolean deleteConcert(int concertId);

    Concert getConcertById(int concertId);

    List<Concert> getConcertsForUser(int userId);

    int countConcertsThisYear(int userId);
}
