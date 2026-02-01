package com.concertapp.dao;

import com.concertapp.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    boolean validateUserLogin(String username, String password);

    boolean usernameExists(String username);

    User getUserById(int userId);

    User getUserByUsername(String username);

    List<String> getUserRolesByUsername(String username);
}
