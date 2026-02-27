package com.concertapp.dao;

import com.concertapp.dto.DayStatsDto;
import com.concertapp.exception.DaoException;
import com.concertapp.model.UserYearlyStats;
import com.concertapp.dto.MonthStatsDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class StatsJdbcDao implements StatsDao {

    private final JdbcTemplate jdbcTemplate;

    public StatsJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserYearlyStats> getYearlyStatsForUser(int userId) {
        String sql = """
                SELECT EXTRACT(YEAR FROM ce.event_date)::int AS year,
                    COUNT(*) AS concerts_attended
                FROM user_concert uc
                JOIN concert_event ce ON ce.concert_event_id = uc.concert_event_id
                WHERE uc.user_id = ?
                GROUP BY year
                ORDER BY year DESC
            """;
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUserYearlyStats(rs), userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Database error getting yearly stats", e);
        }
    }

    @Override
    public List<MonthStatsDto> getMonthlyStatsForUser(int userId, int year) {
        String sql = """
                SELECT EXTRACT(MONTH FROM ce.event_date)::int AS month,
                    COUNT(*) AS concert_count
                FROM user_concert uc
                JOIN concert_event ce ON uc.concert_event_id = ce.concert_event_id
                WHERE uc.user_id = ?
                    AND EXTRACT(YEAR FROM ce.event_date)::int = ?
                GROUP BY month
                ORDER BY month
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MonthStatsDto(
                        rs.getInt("month"),
                        rs.getInt("concert_count")
                ), userId, year);
    }

    @Override
    public List<Integer> getConcertDaysForUser(int userId, int year, int month) {
        String sql = """
                SELECT EXTRACT(DAY FROM ce.event_date)::int AS day
                FROM user_concert uc
                JOIN concert_event ce
                  ON uc.concert_event_id = ce.concert_event_id
                WHERE uc.user_id = ?
                  AND EXTRACT(YEAR FROM ce.event_date)::int = ?
                  AND EXTRACT(MONTH FROM ce.event_date)::int = ?
                ORDER BY day
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("day"),
                userId, year, month);
    }

    @Override
    public List<DayStatsDto> getConcertDetailsForUserOnDate(int userId, LocalDate date) {
        String sql = """
                SELECT
                      ce.concert_event_id AS concert_id, ce.event_date, ce.start_time,
                      a.name AS artist_name, v.name AS venue_name,
                      v.city AS venue_city, v.state AS venue_state,
                      t.name AS tour_name, f.name AS festival_name,
                      COALESCE(array_agg(DISTINCT oa.name) FILTER (WHERE oa.name IS NOT NULL), '{}') AS opening_act_names
                  FROM user_concert uc
                  JOIN concert_event ce ON ce.concert_event_id = uc.concert_event_id
                  JOIN artist a ON a.artist_id = ce.artist_id
                  JOIN venue v ON v.venue_id = ce.venue_id
                  LEFT JOIN tour t ON t.tour_id = ce.tour_id
                  LEFT JOIN festival f ON f.festival_id = ce.festival_id
                  LEFT JOIN concert_opening_act coa ON coa.concert_event_id = ce.concert_event_id
                  LEFT JOIN artist oa ON oa.artist_id = coa.artist_id
                  WHERE uc.user_id = ?
                    AND ce.event_date = ?
                  GROUP BY
                      ce.concert_event_id, ce.event_date, ce.start_time,
                      a.name, v.name, v.city, v.state, t.name, f.name
                  ORDER BY ce.start_time;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DayStatsDto dto = new DayStatsDto();
            dto.setConcertId(rs.getInt("concert_id"));
            dto.setDate(rs.getDate("event_date").toLocalDate());
            dto.setStartTime(rs.getTime("start_time").toLocalTime());
            dto.setArtistName(rs.getString("artist_name"));

            dto.setVenueName(rs.getString("venue_name"));
            dto.setVenueCity(rs.getString("venue_city"));
            dto.setVenueState(rs.getString("venue_state"));

            dto.setTourName(rs.getString("tour_name"));
            dto.setFestivalName(rs.getString("festival_name"));

            java.sql.Array sqlArray = rs.getArray("opening_act_names");
            if (sqlArray != null) {
                String[] arr = (String[]) sqlArray.getArray();
                dto.setOpeningActNames(java.util.Arrays.asList(arr));
            } else {
                dto.setOpeningActNames(java.util.Collections.emptyList());
            }
            return dto;
            }, userId, java.sql.Date.valueOf(date));
    }

    private UserYearlyStats mapRowToUserYearlyStats(ResultSet rs) throws SQLException {
        UserYearlyStats stats = new UserYearlyStats();
        stats.setYear(rs.getInt("year"));
        stats.setConcertCount(rs.getInt("concerts_attended"));
        return stats;
    }

}
