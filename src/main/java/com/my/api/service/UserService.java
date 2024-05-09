package com.my.api.service;

import com.my.api.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    long addUser(User user);
    User updateUser(Long id, User userDetails);
    User updateUserFields(Long id, User updates);
    User getById(long id);
    void deleteById(long id);
    List<User> getUsersByBirthDate(LocalDate start, LocalDate end);
}
