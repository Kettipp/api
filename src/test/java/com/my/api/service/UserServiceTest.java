package com.my.api.service;

import com.my.api.exception.UserAlreadyExistException;
import com.my.api.exception.UserNotFoundException;
import com.my.api.model.User;
import com.my.api.repository.UserRepository;
import com.my.api.repository.impl.UserRepositoryImpl;
import com.my.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    UserRepository repository = new UserRepositoryImpl();
    UserService service = new UserServiceImpl(repository);

    @Test
    public void addUser_shouldThrowUserNotFoundException_whenUserIsAlreadyExist() {
        User user = User.builder().firstName("Miranda").lastName("Smith").email("smith@com.ua").build();
        service.addUser(user);
        assertThrows(UserAlreadyExistException.class, () -> service.addUser(user));
    }

    @Test
    public void updateUser_shouldChangeAllFields() {
        long userId = 1L;
        User user = User.builder()
                .firstName("Miranda")
                .lastName("Smith")
                .email("smith@com.ua")
                .birthDate(LocalDate.of(1999, 4, 25))
                .phoneNumber("0990840476")
                .address("Kharkiv")
                .build();
        service.addUser(user);

        User updates = User.builder()
                .firstName("Updated")
                .lastName("Updated")
                .email("smith@Updated.ua")
                .birthDate(LocalDate.of(1999, 4, 26))
                .phoneNumber("0990840476")
                .address("Updated")
                .build();

        User actual = service.updateUser(userId, updates);
        assertEquals(service.getById(1L), actual);
    }

    @Test
    public void updateUserFields_shouldChangeSomeOfUsersFields() {
        long userId = 1L;
        User user = User.builder().firstName("Miranda").lastName("Smith").email("smith@com.ua").build();
        user.setId(userId);
        service.addUser(user);

        User updates = User.builder().firstName("Updated Name").build();

        User updatedUser = service.updateUserFields(userId, updates);

        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getFirstName());
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
