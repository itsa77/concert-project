package com.concertapp.controller;

import com.concertapp.model.UserYearlyStats;
import com.concertapp.service.StatsService;
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
}
