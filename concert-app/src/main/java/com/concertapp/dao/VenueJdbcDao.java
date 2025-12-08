package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Venue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VenueJdbcDao implements VenueDao{

    private final JdbcTemplate jdbcTemplate;

    public VenueJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Integer getOrCreateVenueId(String venueName, String city, String state){
        String selectSql = """
            SELECT venue_id, name, city, state
            FROM venue 
            WHERE LOWER(name) = LOWER(?) 
              AND LOWER(city) = LOWER(?) 
              AND LOWER(state) = LOWER(?)
            """;
       try{
           SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, venueName, city, state);
           if(rs.next()){
               return rs.getInt("venue_id");
           }
           String insertSql = """
            INSERT INTO venue (name, city, state)
            VALUES (?, ?, ?)
            RETURNING venue_id
            """;
           SqlRowSet insertRs = jdbcTemplate.queryForRowSet(insertSql, venueName, city, state);
           if(insertRs.next()){
               return insertRs.getInt("venue_id");
           }
       } catch (CannotGetJdbcConnectionException e) {
           throw new DaoException("Database connection error", e);
       } catch (DataAccessException e){
           throw new DaoException("Error accessing venue data", e);
       }return null;
    }
    @Override
    public Venue getVenueById(int venueId) {
        Venue venue = null;
        String sql = """
        SELECT venue_id, name, city, state
        FROM venue
        WHERE venue_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, venueId);
            if (rs.next()) {
                venue = mapRowToVenue(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving venue", e);
        } return venue;
    }
    @Override
    public List<Venue> getAllVenues() {
        List<Venue> venues = new ArrayList<>();
        String sql = """
        SELECT venue_id, name, city, state
        FROM venue
        ORDER BY name, city
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                venues.add(mapRowToVenue(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving venues", e);
        } return venues;
    }

    private Venue mapRowToVenue(SqlRowSet rs){
        Venue venue = new Venue();
        venue.setVenueId(rs.getInt("venue_id"));
        venue.setName(rs.getString("name"));
        venue.setCity(rs.getString("city"));
        venue.setState(rs.getString("state"));
        return venue;
    }


}
