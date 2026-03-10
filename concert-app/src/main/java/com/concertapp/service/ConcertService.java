package com.concertapp.service;

import com.concertapp.dao.*;
import com.concertapp.dto.CreateConcertDto;
import com.concertapp.dto.ConcertResponseDto;
import com.concertapp.model.Concert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertDao concertDao;
    private final ArtistDao artistDao;
    private final UserDao userDao;
    private final VenueDao venueDao;
    private final TourDao tourDao;
    private final FestivalDao festivalDao;

    public ConcertService(ConcertDao concertDao, ArtistDao artistDao, UserDao userDao,
                          VenueDao venueDao, TourDao tourDao, FestivalDao festivalDao) {
        this.concertDao = concertDao;
        this.artistDao = artistDao;
        this.userDao = userDao;
        this.venueDao = venueDao;
        this.tourDao = tourDao;
        this.festivalDao = festivalDao;
    }

    @Transactional
    public ConcertResponseDto createConcert(CreateConcertDto dto, String username) {
        int userId = userDao.getUserByUsername(username).getUserId();
        Integer artistId = artistDao.getOrCreateArtistId(dto.getArtistName());

        List<Integer> openingActIds = null;
        if (dto.getOpeningActNames() != null && !dto.getOpeningActNames().isEmpty()) {
            openingActIds = dto.getOpeningActNames().stream()
                    .filter(name -> name != null && !name.isBlank())
                    .map(artistDao::getOrCreateArtistId)
                    .toList();
        }

        Concert concert = new Concert();
        concert.setArtistId(artistId);
        Integer venueId = venueDao.getOrCreateVenueId(
                dto.getVenueName(),
                dto.getVenueCity(),
                dto.getVenueState()
        );
        concert.setVenueId(venueId);
        concert.setDate(dto.getDate());
        concert.setStartTime(dto.getStartTime());
        concert.setCreatedBy(userId);
        concert.setOpeningActIds(openingActIds);

        Concert created = concertDao.createConcert(concert, dto.getTourName(), dto.getFestivalName());
        return mapToResponse(created);
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

    @Transactional(readOnly = true)
    public List<ConcertResponseDto> getConcertsForUser(String username) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return concertDao.getConcertsAttendedByUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConcertResponseDto getConcertByIdForUser(String username, int concertId) {
        int userId = userDao.getUserByUsername(username).getUserId();

        Concert concert = concertDao.getConcertById(concertId);
        if (concert == null) {
            throw new RuntimeException("Concert not found");
        }
        if (!concertDao.userHasAccessToConcert(userId, concertId)) {
            throw new RuntimeException("Forbidden");
        }
        return mapToResponse(concert);
    }

    private ConcertResponseDto mapToResponse(Concert c) {
        ConcertResponseDto dto = new ConcertResponseDto();
        dto.setConcertId(c.getConcertId());
        dto.setDate(c.getDate());
        dto.setStartTime(c.getStartTime());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setArtistName(artistDao.getArtistName(c.getArtistId()));
        var venue = venueDao.getVenueById(c.getVenueId());
        dto.setVenueName(venue.getName());
        dto.setVenueCity(venue.getCity());
        dto.setVenueState(venue.getState());
        if (c.getTourId() != null) {
            dto.setTourName(tourDao.getTourName(c.getTourId()));
        }
        if (c.getFestivalId() != null) {
            dto.setFestivalName(festivalDao.getFestivalName(c.getFestivalId()));
        }
        dto.setCreatedByUsername(userDao.getUsernameByUserId(c.getCreatedBy()));

        if (c.getOpeningActIds() != null) {
            dto.setOpeningActNames(
                    c.getOpeningActIds().stream()
                            .map(artistDao::getArtistName)
                            .toList()
            );
        }
        return dto;
    }

}
