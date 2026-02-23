package com.concertapp.controller;

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

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @PostMapping
    public ConcertResponseDto createConcert(@AuthenticationPrincipal UserDetails ud,
                                            @RequestBody CreateConcertDto dto) {
        return concertService.createConcert(dto, ud.getUsername());
    }

    @PostMapping("/{concertId}/me")
    public void addMeToConcert(@PathVariable int concertId,
                               @AuthenticationPrincipal UserDetails ud) {
        concertService.addUserToConcert(ud.getUsername(), concertId);
    }

    @GetMapping
    public List<ConcertResponseDto> getConcerts(@AuthenticationPrincipal UserDetails ud) {
        return concertService.getConcertsForUser(ud.getUsername());
    }

    @GetMapping("/{concertId}")
    public ConcertResponseDto getConcertById(@PathVariable int concertId,
                                             @AuthenticationPrincipal UserDetails ud) {
        return concertService.getConcertByIdForUser(ud.getUsername(), concertId);
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
}
