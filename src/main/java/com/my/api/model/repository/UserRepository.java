package com.my.api.model.repository;

import com.my.api.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    long save(User user);
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    String deleteById(long id);
    List<User> findUsersByBirthDate(LocalDate start, LocalDate end);
}
