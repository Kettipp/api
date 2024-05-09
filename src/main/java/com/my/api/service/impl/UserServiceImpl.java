package com.my.api.service.impl;

import com.my.api.exception.UserAlreadyExistException;
import com.my.api.exception.UserNotFoundException;
import com.my.api.model.User;
import com.my.api.repository.UserRepository;
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
    private static final String NULL_CHECK_EXCEPTION_MESSAGE = "At least one of User parameters must be provided";
    private static final String GET_USERS_BY_BIRTH_DATE_EXCEPTION_MESSAGE = "Start date should be greater  then end date";
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public long addUser(User user) {
        Optional<User> maybeUser = repository.findByEmail(user.getEmail());
        if (maybeUser.isEmpty()) {
            return repository.save(user);
        } else {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = getById(id);
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setBirthDate(userDetails.getBirthDate());
        user.setAddress(userDetails.getAddress());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        return user;
    }

    @Override
    public User updateUserFields(Long id, User updates) {
        User user = getById(id);
        nullCheck(updates);
        Optional.ofNullable(updates.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(updates.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updates.getBirthDate()).ifPresent(user::setBirthDate);
        Optional.ofNullable(updates.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(updates.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        return user;

    }

    @Override
    public User getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public void deleteById(long id) {
        User user = getById(id);
        repository.deleteById(user.getId());
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate start, LocalDate end) {
        if (start.isBefore(end)) {
            return repository.findUsersByBirthDate(start, end);
        } else {
            throw new InvalidParameterException(GET_USERS_BY_BIRTH_DATE_EXCEPTION_MESSAGE);
        }
    }

    private void nullCheck(User user) {
        if (user.getFirstName() == null && user.getLastName() == null
                && user.getEmail() == null && user.getBirthDate() == null
                && user.getAddress() == null && user.getPhoneNumber() == null) {
            throw new InvalidParameterException(NULL_CHECK_EXCEPTION_MESSAGE);
        }
    }
}
