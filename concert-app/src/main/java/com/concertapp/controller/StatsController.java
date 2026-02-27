package com.concertapp.controller;

import com.concertapp.dto.DayStatsDto;
import com.concertapp.dto.MonthStatsDto;
import com.concertapp.model.UserYearlyStats;
import com.concertapp.service.StatsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stats")
@CrossOrigin
public class StatsController {

    private  final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/yearly")
    public List<UserYearlyStats> getYearly(@AuthenticationPrincipal UserDetails ud) {
        return statsService.getYearlyStatsForUser(ud.getUsername());
    }

    @GetMapping
    public List<MonthStatsDto> getMonthly(@PathVariable int year, @AuthenticationPrincipal UserDetails ud) {
        return statsService.getMonthlyStatsForUser(ud.getUsername(), year);
    }

    @GetMapping
    public List<Integer> getDays(@PathVariable int year, @PathVariable int month,
                                 @AuthenticationPrincipal UserDetails ud) {
        return statsService.getConcertDaysForUser(ud.getUsername(), year, month);
    }

    @GetMapping("/day")
    public List<DayStatsDto> getConcertsForDay(@AuthenticationPrincipal UserDetails ud,
                                               @RequestParam String date
    ) {
        return statsService.getConcertDetailsForUserOnDate(
                ud.getUsername(),java.time.LocalDate.parse(date)
        );
    }
}
