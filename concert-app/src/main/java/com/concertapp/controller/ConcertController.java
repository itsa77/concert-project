package com.concertapp.controller;

import com.concertapp.dao.*;
import com.concertapp.model.Concert;
import com.concertapp.dto.CreateConcertDto;
import com.concertapp.dto.ConcertResponseDto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
@CrossOrigin
public class ConcertController {

    private final ConcertDao concertDao;
    private final ArtistDao artistDao;
    private final VenueDao venueDao;
    private final TourDao tourDao;
    private final FestivalDao festivalDao;
    private final OpeningActDao openingActDao;
    private final UserDao userDao;

    public ConcertController(
            ConcertDao concertDao,
            ArtistDao artistDao,
            VenueDao venueDao,
            TourDao tourDao,
            FestivalDao festivalDao,
            OpeningActDao openingActDao,
            UserDao userDao) {

        this.concertDao = concertDao;
        this.artistDao = artistDao;
        this.venueDao = venueDao;
        this.tourDao = tourDao;
        this.festivalDao = festivalDao;
        this.openingActDao = openingActDao;
        this.userDao = userDao;
    }

    @PostMapping
    public ConcertResponseDto createConcert(
            @AuthenticationPrincipal UserDetails ud,
            @RequestBody CreateConcertDto dto) {
        int userId = userDao.getUserByUsername(ud.getUsername()).getUserId();
        Integer artistId = artistDao.getOrCreateArtistId(dto.getArtistName());
        List<Integer> openingActIds = null;
        if (dto.getOpeningActNames() != null) {
            openingActIds = dto.getOpeningActNames().stream()
                    .map(name -> artistDao.getOrCreateArtistId(name))
                    .toList();
        }
        Concert concert = new Concert();
        concert.setUserId(userId);
        concert.setArtistId(artistId);
        concert.setVenueId(dto.getVenueId());
        concert.setDate(dto.getDate());
        concert.setNotes(dto.getNotes());
        concert.setOpeningActIds(openingActIds);
        Concert created = concertDao.createConcert(
                concert,
                dto.getTourName(),
                dto.getFestivalName()
        );
        return mapToResponse(created);
    }

    @GetMapping
    public List<ConcertResponseDto> getConcerts(@AuthenticationPrincipal UserDetails ud) {
        int userId = userDao.getUserByUsername(ud.getUsername()).getUserId();
        return concertDao.getConcertsForUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/{concertId}")
    public ConcertResponseDto getConcertById(@PathVariable int concertId,
                                             @AuthenticationPrincipal UserDetails ud) {
        Concert concert = concertDao.getConcertById(concertId);
        if (concert == null) {
            throw new RuntimeException("Concert not found");
        }
        int userId = userDao.getUserByUsername(ud.getUsername()).getUserId();
        if (concert.getUserId() != userId) {
            throw new RuntimeException("Forbidden");
        } return mapToResponse(concert);
    }

    @DeleteMapping("/{concertId}")
    public void deleteConcert(@PathVariable int concertId,
                              @AuthenticationPrincipal UserDetails ud) {
        Concert c = concertDao.getConcertById(concertId);
        int userId = userDao.getUserByUsername(ud.getUsername()).getUserId();
        if (c == null || c.getUserId() != userId) {
            throw new RuntimeException("Forbidden");
        }
        concertDao.deleteConcert(concertId);
    }

    private ConcertResponseDto mapToResponse(Concert c) {
        ConcertResponseDto dto = new ConcertResponseDto();
        dto.setConcertId(c.getConcertId());
        dto.setDate(c.getDate());
        dto.setNotes(c.getNotes());
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
        if (c.getOpeningActIds() != null) {
            dto.setOpeningActNames(
                    c.getOpeningActIds().stream()
                            .map(id -> artistDao.getArtistName(id))
                            .toList()
            );
        } return dto;
    }
}
