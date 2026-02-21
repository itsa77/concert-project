package com.concertapp.dao;

import com.concertapp.model.Concert;

import java.util.List;

public interface ConcertDao {
    Concert createConcert(Concert concert, String tourName, String festivalName);

    boolean deleteConcert(int concertId);

    boolean addUserToConcert(int userId, int concertId);

    boolean removeUserFromConcert(int userId, int concertId);

    Concert getConcertById(int concertId);

    List<Concert> getConcertsAttendedByUser(int userId);

    int countConcertsAttendedThisYear(int userId);

    boolean userHasAccessToConcert(int userId, int concertId);

}
