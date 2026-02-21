package com.concertapp.controller;

import com.concertapp.dao.*;
import com.concertapp.model.Concert;
import com.concertapp.dto.CreateConcertDto;
import com.concertapp.dto.ConcertResponseDto;
import com.concertapp.service.ConcertService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/concerts")
@CrossOrigin
public class ConcertController {

    private final ConcertService concertService;
    private final ConcertDao concertDao;
    private final ArtistDao artistDao;
    private final VenueDao venueDao;
    private final TourDao tourDao;
    private final FestivalDao festivalDao;
    private final UserDao userDao;

    public ConcertController(ConcertService concertService, ConcertDao concertDao, ArtistDao artistDao, VenueDao venueDao,
            TourDao tourDao, FestivalDao festivalDao, UserDao userDao) {
        this.concertService = concertService;
        this.concertDao = concertDao;
        this.artistDao = artistDao;
        this.venueDao = venueDao;
        this.tourDao = tourDao;
        this.festivalDao = festivalDao;
        this.userDao = userDao;
    }

    @PostMapping
    public ConcertResponseDto createConcert(@AuthenticationPrincipal UserDetails ud,
                                            @RequestBody CreateConcertDto dto) {
        Concert created = concertService.createConcert(dto, ud.getUsername());
        return mapToResponse(created);
    }

    @PostMapping("/{concertId}/me")
    public void addMeToConcert(@PathVariable int concertId,
                               @AuthenticationPrincipal UserDetails ud) {
        concertService.addUserToConcert(ud.getUsername(), concertId);
    }

    @GetMapping
    public List<ConcertResponseDto> getConcerts(@AuthenticationPrincipal UserDetails ud) {
        int userId = userDao.getUserByUsername(ud.getUsername()).getUserId();
        return concertDao.getConcertsAttendedByUser(userId)
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
        if (!concertDao.userHasAccessToConcert(userId, concertId)) {
            throw new RuntimeException("Forbidden");
        } return mapToResponse(concert);
    }

    @DeleteMapping("/{concertId}")
    public void deleteConcert(@PathVariable int concertId,
                                     @AuthenticationPrincipal UserDetails ud) {
        concertService.deleteConcert(ud.getUsername(), concertId);
    }

    @DeleteMapping("/{concertId}/me")
    public void removeUserFromConcert(@PathVariable int concertId,
                                    @AuthenticationPrincipal UserDetails ud) {
        concertService.removeUserFromConcert(ud.getUsername(), concertId);
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
            dto.setOpeningActNames(c.getOpeningActIds().stream()
                            .map(artistDao::getArtistName)
                            .toList()
            );
        } return dto;
    }
}
