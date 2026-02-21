package com.concertapp.service;

import com.concertapp.dao.*;
import com.concertapp.dto.CreateConcertDto;
import com.concertapp.model.Concert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertDao concertDao;
    private final ArtistDao artistDao;
    private final UserDao userDao;

    public ConcertService(ConcertDao concertDao, ArtistDao artistDao, UserDao userDao) {
        this.concertDao = concertDao;
        this.artistDao = artistDao;
        this.userDao = userDao;
    }
    @Transactional
    public Concert createConcert(CreateConcertDto dto, String username) {
        int userId = userDao.getUserByUsername(username).getUserId();
        Integer artistId = artistDao.getOrCreateArtistId(dto.getArtistName());

        List<Integer> openingActIds = null;
        if (dto.getOpeningActNames() != null && !dto.getOpeningActNames().isEmpty()) {
            openingActIds = dto.getOpeningActNames().stream()
                    .filter(name -> name != null && !name.isBlank())
                    .map(artistDao::getOrCreateArtistId).toList();
        }
        Concert concert = new Concert();
        concert.setArtistId(artistId);
        concert.setVenueId(dto.getVenueId());
        concert.setDate(dto.getDate());
        concert.setStartTime(dto.getStartTime());
        concert.setCreatedBy(userId);
        concert.setOpeningActIds(openingActIds);
        return concertDao.createConcert(concert, dto.getTourName(), dto.getFestivalName());
    }

    @Transactional
    public void addUserToConcert(String username, int concertId) {
        int userId = userDao.getUserByUsername(username).getUserId();

        Concert concert = concertDao.getConcertById(concertId);
        if (concert == null) {
            throw new RuntimeException("Concert not found");
        }
        if (concert.getCreatedBy() == userId) {
            throw new RuntimeException("Creator is already attending this concert");
        }
        boolean added = concertDao.addUserToConcert(userId, concertId);
        if (!added) {
            throw new RuntimeException("You are already attending this concert");
        }
    }

    @Transactional
    public void removeUserFromConcert(String username, int concertId) {
        int userId = userDao.getUserByUsername(username).getUserId();

        Concert concert = concertDao.getConcertById(concertId);
        if (concert == null) {
            throw new RuntimeException("Concert not found");
        }
        if (concert.getCreatedBy() == userId) {
            throw new RuntimeException("Creator cannot remove themself from their own event");
        }
        boolean removed = concertDao.removeUserFromConcert(userId, concertId);
        if (!removed) {
            throw new RuntimeException("You are not attending this concert");
        }
    }

    @Transactional
    public void deleteConcert(String username, int concertId) {
        int userId = userDao.getUserByUsername(username).getUserId();
        Concert concert = concertDao.getConcertById(concertId);

        if (concert == null) {
            throw new RuntimeException("Concert not found");
        }
        if (concert.getCreatedBy() != userId) {
            throw new RuntimeException("Forbidden");
        }
        boolean deleted = concertDao.deleteConcert(concertId);
        if (!deleted) {
            throw new RuntimeException("Concert not found");
        }
    }



}
