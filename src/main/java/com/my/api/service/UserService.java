package com.my.api.service;

import com.my.api.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    long addUser(User user);
    User getById(long id);
    String deleteById(long id);
    List<User> getUsersByBirthDate(LocalDate start, LocalDate end);
}
