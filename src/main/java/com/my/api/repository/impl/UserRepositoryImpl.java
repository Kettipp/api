package com.my.api.repository.impl;

import com.my.api.model.User;
import com.my.api.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
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
    public Optional<User> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }

    @Override
    public void deleteById(long id) {
         storage.remove(id);
    }

    @Override
    public List<User> findUsersByBirthDate(LocalDate start, LocalDate end) {
        return storage.values()
                .stream()
                .filter(u -> u.getBirthDate().isAfter(start) && u.getBirthDate().isBefore(end))
                .toList();
    }
}
