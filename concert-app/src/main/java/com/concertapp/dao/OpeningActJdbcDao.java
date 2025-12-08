package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OpeningActJdbcDao implements OpeningActDao{

    private final JdbcTemplate jdbcTemplate;

    public OpeningActJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void insertOpeningActs(int concertId, List<Integer> actIds) {
        if (actIds == null || actIds.isEmpty()) return;
        String sql = """
        INSERT INTO concert_opening_act (concert_id, artist_id)
        VALUES (?, ?)
        """;
        try {
            for (Integer id : actIds) {
                if (id == null) continue;
                jdbcTemplate.update(sql, concertId, id);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error inserting opening acts", e);
        }
    }
    @Override
    public List<Integer> getOpeningActIds(int concertId) {
        List<Integer> ids = new ArrayList<>();
        String sql = """
                SELECT artist_id
                FROM concert_opening_act
                WHERE concert_id = ?
                """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, concertId);
            while (rs.next()) {
                ids.add(rs.getInt("artist_id"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving opening acts", e);
        }
        return ids;
    }



}
