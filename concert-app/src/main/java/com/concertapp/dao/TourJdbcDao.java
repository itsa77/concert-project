package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Tour;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TourJdbcDao implements TourDao{

    private final JdbcTemplate jdbcTemplate;

    public TourJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Integer getOrCreateTourId(String name) {
        if (name == null || name.isBlank()) return null;
        String selectSql = """
        SELECT tour_id
        FROM tour
        WHERE LOWER(name) = LOWER(?)
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, name);
            if (rs.next()) {
                return rs.getInt("tour_id");
            }
            String insertSql = """
            INSERT INTO tour (name)
            VALUES (?)
            RETURNING tour_id
            """;
            SqlRowSet insertRs = jdbcTemplate.queryForRowSet(insertSql, name);
            if (insertRs.next()) {
                return insertRs.getInt("tour_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error inserting or retrieving tour", e);
        } return null;
    }
    @Override
    public List<Tour> getAllTours() {
        List<Tour> tours = new ArrayList<>();
        String sql = """
        SELECT tour_id, name
        FROM tour
        ORDER BY name
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                tours.add(mapRowToTour(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving tours", e);
        } return tours;
    }
    @Override
    public Tour getTourById(int tourId) {
        Tour tour = null;
        String sql = """
        SELECT tour_id, name
        FROM tour
        WHERE tour_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, tourId);
            if (rs.next()) {
                tour = mapRowToTour(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving tour", e);
        } return tour;
    }

    @Override
    public String getTourName(int tourId) {
        String sql = """
        SELECT name
        FROM tour
        WHERE tour_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, tourId);
            if (rs.next()) {
                return rs.getString("name");
            } return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database retrieving tour name", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error retrieving tour name", e);
        }
    }


    private Tour mapRowToTour(SqlRowSet rs) {
        Tour tour = new Tour();
        tour.setTourId(rs.getInt("tour_id"));
        tour.setName(rs.getString("name"));
        return tour;
    }




}
