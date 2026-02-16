package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.Concert;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Time;
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
            INSERT INTO concert_event (artist_id, venue_id, event_date, start_time, tour_id, festival_id, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING concert_event_id, created_at
            """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                    concert.getArtistId(),
                    concert.getVenueId(),
                    concert.getDate(),
                    concert.getStartTime(),
                    concert.getTourId(),
                    concert.getFestivalId(),
                    concert.getCreatedBy()
            );
            if (rs.next()) {
                concert.setConcertId(rs.getInt("concert_event_id"));
                concert.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                String userConcertSql = """
                        INSERT INTO user_concert (user_id, concert_event_id)
                        VALUES (?, ?)
                        """;
                jdbcTemplate.update(
                        userConcertSql,
                        concert.getCreatedBy(),
                        concert.getConcertId()
                );
                if (concert.getOpeningActIds() != null && !concert.getOpeningActIds().isEmpty()) {
                    openingActDao.insertOpeningActs(concert.getConcertId(), concert.getOpeningActIds());
                }
                return concert;
            } throw new DaoException("Failed to create concert (no id returned)");
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when creating concert", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error creating concert", e);
        }
    }

    @Override
    public boolean deleteConcert(int concertId) {

        String sql = """
            DELETE FROM concert_event
            WHERE concert_event_id = ?
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
    public boolean removeUserFromConcert(int userId, int concertId) {
        String sql = """
                DELETE FROM user_concert
                WHERE user_id = ?
                   AND concert_event_id = ?
                """;
        try {
            int rows = jdbcTemplate.update(sql, userId, concertId);
            return  rows > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when removing user from concert", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error removing user from concert", e);
        }
    }

    @Override
    public Concert getConcertById(int concertId) {
        String sql = """
        SELECT concert_event_id, artist_id, venue_id, event_date, start_time, tour_id, festival_id,
               created_by, created_at
        FROM concert_event
        WHERE concert_event_id = ?
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
    public List<Concert> getConcertsAttendedByUser(int userId) {
        List<Concert> concerts = new ArrayList<>();
        String sql = """
            SELECT ce.concert_event_id, ce.artist_id, ce.venue_id, ce.event_date, ce.start_time,
                   ce.tour_id, ce.festival_id, ce.created_by, ce.created_at
            FROM user_concert uc
            JOIN concert_event ce ON ce.concert_event_id = uc.concert_event_id
            WHERE uc.user_id = ?
            ORDER BY ce.event_date DESC
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
    public int countConcertsAttendedThisYear(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM user_concert uc
            JOIN concert_event ce ON ce.concert_event_id = uc.concert_event_id
            WHERE uc.user_id = ?
              AND ce.event_date >= date_trunc('year', CURRENT_DATE)
              AND ce.event_date <  date_trunc('year', CURRENT_DATE) + INTERVAL '1 year'
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

    @Override
    public boolean userHasAccessToConcert(int userId, int concertId) {
        String sql = """
        SELECT EXISTS (
            SELECT 1
            FROM concert_event ce
            LEFT JOIN user_concert uc
                ON uc.concert_event_id = ce.concert_event_id
               AND uc.user_id = ?
            WHERE ce.concert_event_id = ?
              AND (uc.user_id IS NOT NULL OR ce.created_by = ?))
        """;
        try {
            return Boolean.TRUE.equals(
                    jdbcTemplate.queryForObject(sql, Boolean.class, userId, concertId, userId));
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database when checking concert access", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error when checking concert access", e);
        }
    }

    private Concert mapRowToConcert(SqlRowSet rs) {
        Concert concert = new Concert();

        concert.setConcertId(rs.getInt("concert_event_id"));
        concert.setArtistId(rs.getInt("artist_id"));
        concert.setVenueId(rs.getInt("venue_id"));

        LocalDate date = rs.getDate("event_date").toLocalDate();
        concert.setDate(date);

        Time t = rs.getTime("start_time");
        concert.setStartTime(t == null ? null : t.toLocalTime());

        concert.setTourId((Integer) rs.getObject("tour_id"));
        concert.setFestivalId((Integer) rs.getObject("festival_id"));

        concert.setCreatedBy(rs.getInt("created_by"));

        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        concert.setCreatedAt(createdAt);

        return concert;
    }
}
