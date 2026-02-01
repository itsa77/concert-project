package com.concertapp.dao;

import com.concertapp.exception.DaoException;
import com.concertapp.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserJdbcDao implements UserDao{

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User createUser(User user) {

        String sql = """
            INSERT INTO users (username, first_name, last_name, email, password_hash)
            VALUES (?, ?, ?, ?, ?)
            RETURNING user_id, total_concerts, wanting_email_updates, created_at
            """;
        String roleSql = """
            INSERT INTO user_roles (user_id, role_id)
            VALUES (?, (SELECT role_id FROM roles WHERE name = 'USER'))
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPasswordHash()
            );
            if (rs.next()) {
                user.setUserId(rs.getInt("user_id"));
                user.setTotalConcerts(rs.getInt("total_concerts"));
                user.setWantingEmailUpdates(rs.getBoolean("wanting_email_updates"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                jdbcTemplate.update(roleSql, user.getUserId());

                return user;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error creating user", e);
        }
        return null;
    }

    @Override
    public List<String> getUserRolesByUsername(String username) {
        String sql = """
            SELECT r.name FROM roles r
            JOIN user_roles ur ON ur.role_id = r.role_id
            JOIN users u ON u.user_id = ur.user_id
            WHERE LOWER(u.username) = LOWER(?);
            """;
        try {
            return jdbcTemplate.queryForList(sql, String.class, username);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error getting user roles", e);
        }
    }

    @Override
    public boolean validateUserLogin(String username, String password) {
        String sql = """
        SELECT password_hash
        FROM users
        WHERE LOWER(username) = LOWER(?)
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return BCrypt.checkpw(password, storedHash);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error validating user login", e);
        } return false;
    }

    @Override
    public boolean usernameExists(String username) {
        String sql = """
        SELECT COUNT(*) AS count
        FROM users
        WHERE LOWER(username) = LOWER(?)
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error checking username existence", e);
        } return false;
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = """
        SELECT user_id, username, first_name, last_name, email,
               total_concerts, wanting_email_updates, created_at
        FROM users
        WHERE user_id = ?
        """;
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            if (rs.next()) {
                user = mapRowToUser(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving user", e);
        } return user;
    }

    @Override
    public User getUserByUsername(String username) {

        String sql = """
        SELECT user_id, username, first_name, last_name, email, password_hash,
               total_concerts, wanting_email_updates, created_at
        FROM users
        WHERE LOWER(username) = LOWER(?)
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving user by username", e);
        }

        return null;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setTotalConcerts(rs.getInt("total_concerts"));
        user.setWantingEmailUpdates(rs.getBoolean("wanting_email_updates"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }



}
