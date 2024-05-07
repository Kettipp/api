package com.my.api;

import com.my.api.exception.UserAlreadyExistException;
import com.my.api.exception.UserNotFoundException;
import com.my.api.model.User;
import com.my.api.model.repository.UserRepository;
import com.my.api.model.repository.impl.UserRepositoryImpl;
import com.my.api.service.UserService;
import com.my.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    UserRepository repository = new UserRepositoryImpl();
    UserService service = new UserServiceImpl(repository);

    @Test()
    public void addUser_shouldThrowUserNotFoundException_whenUserIsAlreadyExist() {
        User user = User.builder().firstName("Miranda").lastName("Smith").email("smith@com.ua").build();
        service.addUser(user);
        assertThrows(UserAlreadyExistException.class, () -> service.addUser(user));
    }

    @Test
    public void getById_shouldThrowUserNotFoundException_whenUserIsNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.getById(1));
    }

    @Test
    public void deleteById_shouldThrowUserNotFoundException_whenUserIsNotExist() {
        assertThrows(UserNotFoundException.class, () -> service.deleteById(1));
    }

    @Test
    public void getUsersByBirthDate_shouldThrowInvalidParameterException_whenStartDateIsGreaterThenEndDate() {
        assertThrows(InvalidParameterException.class, () ->
                service.getUsersByBirthDate(
                        LocalDate.of(2024, 4, 25),
                        LocalDate.of(1999, 4, 25)
                )
        );
    }
}
