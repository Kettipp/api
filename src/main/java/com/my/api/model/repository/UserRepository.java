package com.my.api.model.repository;

import com.my.api.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository {
    long save(User user);
    User findById(long id);
    User findByEmail(String email);
    String deleteById(long id);
    List<User> findUsersByBirthDate(LocalDate start, LocalDate end);
}
