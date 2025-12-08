package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Artist;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArtistJdbcDao implements ArtistDao {

    private final JdbcTemplate jdbcTemplate;

    public ArtistJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getOrCreateArtistId(String artistName) {
        if (artistName == null || artistName.isBlank()) return null;
        String selectSql = """
                SELECT artist_id
                FROM artist
                WHERE LOWER(name) = LOWER(?)
                """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, artistName);
            if (rs.next()) {
                return rs.getInt("artist_id");
            }
            String insertSql = """
                    INSERT INTO artist (name)
                    VALUES (?)
                    RETURNING artist_id
                    """;
            SqlRowSet insertRs = jdbcTemplate.queryForRowSet(insertSql, artistName);
            if (insertRs.next()) {
                return insertRs.getInt("artist_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error inserting or retrieving artist", e);
        }
        return null;
    }

    @Override
    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        String sql = """
                SELECT artist_id, name
                FROM artist
                ORDER BY name
                """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                artists.add(mapRowToArtist(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving artists", e);
        }
        return artists;
    }

    @Override
    public List<Artist> searchArtistsByName(String searchTerm) {
        List<Artist> results = new ArrayList<>();
        String sql = """
        SELECT artist_id, name
        FROM artist
        WHERE LOWER(name) LIKE LOWER(?) 
        ORDER BY name
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, "%" + searchTerm + "%");
            while (rs.next()) {
                Artist a = new Artist();
                a.setArtistId(rs.getInt("artist_id"));
                a.setName(rs.getString("name"));
                results.add(a);
            } return results;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when searching artists", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error during artist search", e);
        }
    }

    @Override
    public String getArtistName(int artistId) {
        String sql = """
        SELECT name
        FROM artist
        WHERE artist_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, artistId);
            if (rs.next()) {
                return rs.getString("name");
            } return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database retrieving artist name", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error retrieving artist name", e);
        }
    }

    private Artist mapRowToArtist(SqlRowSet rs) {
        Artist artist = new Artist();
        artist.setArtistId(rs.getInt("artist_id"));
        artist.setName(rs.getString("name"));
        return artist;
    }


}