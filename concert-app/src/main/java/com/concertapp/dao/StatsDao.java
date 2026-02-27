package com.concertapp.dao;

import com.concertapp.dto.DayStatsDto;
import com.concertapp.model.UserYearlyStats;
import com.concertapp.dto.MonthStatsDto;
import java.util.List;

public interface StatsDao {
    List<UserYearlyStats> getYearlyStatsForUser(int userId);

    List<MonthStatsDto> getMonthlyStatsForUser(int userId, int year);

    List<Integer> getConcertDaysForUser(int userId, int year, int month);

    List<DayStatsDto> getConcertDetailsForUserOnDate(int userId, java.time.LocalDate date);
}
