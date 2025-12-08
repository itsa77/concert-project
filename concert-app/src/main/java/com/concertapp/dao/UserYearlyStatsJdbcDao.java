package com.concertapp.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserYearlyStatsJdbcDao implements UserYearlyStatsDao{

    private final JdbcTemplate jdbcTemplate;

    public UserYearlyStatsJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }




}
