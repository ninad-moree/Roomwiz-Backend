package com.hotels.roomwiz.service;

import java.util.List;

import com.hotels.roomwiz.model.User;

public interface IUserService {
    User registeredUser(User user);

    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}
