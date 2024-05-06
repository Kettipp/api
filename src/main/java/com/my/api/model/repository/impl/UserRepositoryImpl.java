package com.my.api.model.repository.impl;

import com.my.api.exception.UserNotFoundException;
import com.my.api.model.User;
import com.my.api.model.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    private static final String DELETE_SUCCESS_MESSAGE = "Success";
    private final Map<Long, User> storage = new HashMap<>();
    private long lastId = 0;
    @Override
    public long save(User user) {
        long id = ++lastId;
        user.setId(id);
        storage.put(id, user);
        return id;
    }

    @Override
    public User findById(long id) {
        return Optional
                .of(storage.get(id))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public User findByEmail(String email) {
        return storage.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public String deleteById(long id) {
         storage.remove(id);
        return DELETE_SUCCESS_MESSAGE;
    }

    @Override
    public List<User> findUsersByBirthDate(LocalDate start, LocalDate end) {
        return storage.values()
                .stream()
                .filter(u -> u.getBirthDate().isAfter(start) && u.getBirthDate().isBefore(end))
                .toList();
    }
}
