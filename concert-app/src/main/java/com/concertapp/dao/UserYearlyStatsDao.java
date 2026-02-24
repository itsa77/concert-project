package com.concertapp.dao;

import com.concertapp.model.UserYearlyStats;
import java.util.List;

public interface UserYearlyStatsDao {
    List<UserYearlyStats> getYearlyStatsForUser(int userId);

}
