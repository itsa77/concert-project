package com.concertapp.service;

import com.concertapp.dao.UserDao;
import com.concertapp.dao.UserYearlyStatsDao;
import com.concertapp.model.UserYearlyStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StatsService {

    private final UserDao userDao;
    private final UserYearlyStatsDao userYearlyStatsDao;

    public StatsService(UserDao userDao, UserYearlyStatsDao userYearlyStatsDao) {
        this.userDao = userDao;
        this.userYearlyStatsDao = userYearlyStatsDao;
    }

    @Transactional(readOnly = true)
    public List<UserYearlyStats> getYearlyStatsForUser(String username) {
        int userId = userDao.getUserByUsername(username).getUserId();
        return userYearlyStatsDao.getYearlyStatsForUser(userId);
    }
}
