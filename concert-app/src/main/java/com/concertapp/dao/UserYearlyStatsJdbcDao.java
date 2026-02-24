package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.UserYearlyStats;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserYearlyStatsJdbcDao implements UserYearlyStatsDao{

    private final JdbcTemplate jdbcTemplate;

    public UserYearlyStatsJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserYearlyStats> getYearlyStatsForUser(int userId) {
        String sql = """
                SELECT EXTRACT(YEAR FROM ce.date) AS year,
                    COUNT(*) AS concerts_attended
                FROM user_concert uc
                JOIN concert_event ce ON ce.concert_event_id = uc.concert_event_id
                WHERE uc.user_id = ?
                GROUP BY EXTRACT(YEAR FROM ce.date)
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

    private UserYearlyStats mapRowToUserYearlyStats(ResultSet rs) throws SQLException {
        UserYearlyStats stats = new UserYearlyStats();
        stats.setYear(rs.getInt("year"));
        stats.setConcertCount(rs.getInt("concerts_attended"));
        return stats;
    }




}
