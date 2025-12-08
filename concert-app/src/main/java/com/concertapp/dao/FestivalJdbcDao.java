package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Festival;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FestivalJdbcDao implements FestivalDao{

    private final JdbcTemplate jdbcTemplate;

    public FestivalJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getOrCreateFestivalId(String name) {
        if (name == null || name.isBlank()) return null;
        String selectSql = """
                SELECT festival_id
                FROM festival
                WHERE LOWER(name) = LOWER(?)
                """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, name);
            if (rs.next()) {
                return rs.getInt("festival_id");
            }
            String insertSql = """
                    INSERT INTO festival (name)
                    VALUES (?)
                    RETURNING festival_id
                    """;
            SqlRowSet insertRs = jdbcTemplate.queryForRowSet(insertSql, name);
            if (insertRs.next()) {
                return insertRs.getInt("festival_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error inserting or retrieving festival", e);
        }
        return null;
    }

    @Override
    public List<Festival> getAllFestivals() {
        List<Festival> festivals = new ArrayList<>();
        String sql = """
        SELECT festival_id, name
        FROM festival
        ORDER BY name
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                festivals.add(mapRowToFestival(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving festivals", e);
        } return festivals;
    }

    @Override
    public Festival getFestivalById(int id) {
        Festival festival = null;
        String sql = """
        SELECT festival_id, name
        FROM festival
        WHERE festival_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
            if (rs.next()) {
                festival = mapRowToFestival(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving festival", e);
        } return festival;
    }

    @Override
    public String getFestivalName(int festivalId) {
        String sql = """
        SELECT name
        FROM festival
        WHERE festival_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, festivalId);
            if (rs.next()) {
                return rs.getString("name");
            } return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database retrieving festival name", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error retrieving festival name", e);
        }
    }



    private Festival mapRowToFestival(SqlRowSet rs) {
        Festival festival = new Festival();
        festival.setFestivalId(rs.getInt("festival_id"));
        festival.setName(rs.getString("name"));
        return festival;
    }
}
