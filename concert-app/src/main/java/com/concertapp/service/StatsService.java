package com.concertapp.service;

import com.concertapp.dao.UserDao;
import com.concertapp.dao.StatsDao;
import com.concertapp.dto.DayStatsDto;
import com.concertapp.dto.MonthStatsDto;
import com.concertapp.model.UserYearlyStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatsService {

    private final UserDao userDao;
    private final StatsDao statsDao;

    public StatsService(UserDao userDao, StatsDao statsDao) {
        this.userDao = userDao;
        this.statsDao = statsDao;
    }

    @Transactional(readOnly = true)
    public List<UserYearlyStats> getYearlyStatsForUser(String username) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return statsDao.getYearlyStatsForUser(userId);
    }

    @Transactional(readOnly = true)
    public List<MonthStatsDto> getMonthlyStatsForUser(String username, int year) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return statsDao.getMonthlyStatsForUser(userId, year);
    }

    @Transactional(readOnly = true)
    public List<Integer> getConcertDaysForUser(String username, int year, int month) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return statsDao.getConcertDaysForUser(userId, year, month);
    }

    @Transactional(readOnly = true)
    public List<DayStatsDto> getConcertDetailsForUserOnDate(String username, LocalDate date) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return statsDao.getConcertDetailsForUserOnDate(userId, date);
    }
}
