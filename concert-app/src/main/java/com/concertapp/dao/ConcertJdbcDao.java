package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Concert;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConcertJdbcDao implements ConcertDao {

    private final JdbcTemplate jdbcTemplate;
    private final TourDao tourDao;
    private final FestivalDao festivalDao;
    private final OpeningActDao openingActDao;

    public ConcertJdbcDao(JdbcTemplate jdbcTemplate, TourDao tourDao, FestivalDao festivalDao, OpeningActDao openingActDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.tourDao = tourDao;
        this.festivalDao = festivalDao;
        this.openingActDao = openingActDao;
    }

    @Override
    public Concert createConcert(Concert concert, String tourName, String festivalName) {
        try {
            Integer tourId = null;
            if (tourName != null && !tourName.isBlank()) {
                tourId = tourDao.getOrCreateTourId(tourName);
            }
            Integer festivalId = null;
            if (festivalName != null && !festivalName.isBlank()) {
                festivalId = festivalDao.getOrCreateFestivalId(festivalName);
            }
            concert.setTourId(tourId);
            concert.setFestivalId(festivalId);

        } catch (DataAccessException e) {
            throw new DaoException("Error resolving tour/festival", e);
        }
        String sql = """
            INSERT INTO concert (user_id, artist_id, venue_id, tour_id, festival_id, date, notes, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
            RETURNING concert_id, created_at
            """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                    concert.getUserId(),
                    concert.getArtistId(),
                    concert.getVenueId(),
                    concert.getTourId(),
                    concert.getFestivalId(),
                    concert.getDate(),
                    concert.getNotes()
            );
            if (rs.next()) {
                concert.setConcertId(rs.getInt("concert_id"));
                concert.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (concert.getOpeningActIds() != null && !concert.getOpeningActIds().isEmpty()) {
                openingActDao.insertOpeningActs(concert.getConcertId(), concert.getOpeningActIds());
            } return concert;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when creating concert", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error creating concert", e);
        }
    }

    @Override
    public boolean deleteConcert(int concertId) {

        String sql = """
            DELETE FROM concert
            WHERE concert_id = ?
            """;
        try {
            int rows = jdbcTemplate.update(sql, concertId);
            return rows > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when deleting concert", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error deleting concert", e);
        }
    }

    @Override
    public Concert getConcertById(int concertId) {
        String sql = """
        SELECT concert_id, user_id, artist_id, venue_id, tour_id, festival_id,
               date, notes, created_at
        FROM concert
        WHERE concert_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, concertId);
            if (rs.next()) {
                Concert concert = mapRowToConcert(rs);
                concert.setOpeningActIds(openingActDao.getOpeningActIds(concertId));
                return concert;
            } return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when retrieving concert by ID", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error retrieving concert by ID", e);
        }
    }

    @Override
    public List<Concert> getConcertsForUser(int userId) {
        List<Concert> concerts = new ArrayList<>();
        String sql = """
            SELECT concert_id, user_id, artist_id, venue_id, tour_id, festival_id,
                   date, notes, created_at
            FROM concert
            WHERE user_id = ?
            ORDER BY date DESC
            """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            while (rs.next()) {
                Concert c = mapRowToConcert(rs);
                c.setOpeningActIds(openingActDao.getOpeningActIds(c.getConcertId()));
                concerts.add(c);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when getting concerts", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error retrieving concerts", e);
        } return concerts;
    }

    @Override
    public int countConcertsThisYear(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM concert
            WHERE user_id = ?
              AND date >= date_trunc('year', CURRENT_DATE)
              AND date <  date_trunc('year', CURRENT_DATE) + INTERVAL '1 year'
            """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect when counting concerts", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error counting concerts", e);
        } return 0;
    }

    private Concert mapRowToConcert(SqlRowSet rs) {
        Concert concert = new Concert();
        concert.setConcertId(rs.getInt("concert_id"));
        concert.setUserId(rs.getInt("user_id"));
        concert.setArtistId(rs.getInt("artist_id"));
        concert.setVenueId(rs.getInt("venue_id"));

        LocalDate date = rs.getDate("date").toLocalDate();
        concert.setDate(date);

        LocalDateTime created = rs.getTimestamp("created_at").toLocalDateTime();
        concert.setCreatedAt(created);

        concert.setTourId((Integer) rs.getObject("tour_id"));
        concert.setFestivalId((Integer) rs.getObject("festival_id"));
        concert.setNotes(rs.getString("notes"));
        return concert;
    }
}
