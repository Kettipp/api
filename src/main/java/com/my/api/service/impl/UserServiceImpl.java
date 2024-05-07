package com.my.api.service.impl;

import com.my.api.exception.UserAlreadyExistException;
import com.my.api.exception.UserNotFoundException;
import com.my.api.model.User;
import com.my.api.model.repository.UserRepository;
import com.my.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    private static final String USER_ALREADY_EXIST_EXCEPTION_MESSAGE = "User already exist";
    private static final String INVALID_PARAMETER_EXCEPTION_MESSAGE = "Start date should be greater  then end date";
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public long addUser(User user) {
        Optional<User> maybeUser = repository.findByEmail(user.getEmail());
        if(maybeUser.isEmpty()) {
            return repository.save(user);
        } else {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public User getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public String deleteById(long id) {
        Optional<User> maybeUser = repository.findById(id);
        if(maybeUser.isPresent()) {
            return repository.deleteById(id);
        } else {
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate start, LocalDate end) {
        if(start.isBefore(end)) {
            return repository.findUsersByBirthDate(start, end);
        } else {
            throw new InvalidParameterException(INVALID_PARAMETER_EXCEPTION_MESSAGE);
        }
    }
}
