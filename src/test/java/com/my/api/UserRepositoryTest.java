package com.my.api;

import com.my.api.model.User;
import com.my.api.model.repository.UserRepository;
import com.my.api.model.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserRepositoryTest {
    private final UserRepository repository = new UserRepositoryImpl();

    @Test
    public void save_shouldIncrementId() {
        repository.save(User.builder().firstName("Miranda").lastName("Smith").build());
        long actualId = repository.save(User.builder().firstName("Antonio").lastName("Smith").build());

        long expectedId = 2;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void findById_shouldReturnUser() {
        User expected = User.builder().firstName("Miranda").lastName("Smith").build();
        repository.save(expected);

        User actual = repository.findById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    public void findByEmail_shouldReturnUser() {
        User expected = User.builder().firstName("Miranda").lastName("Smith").email("smith@com.ua").build();
        repository.save(expected);

        User actual = repository.findByEmail("smith@com.ua").get();
        assertEquals(expected, actual);
    }

    @Test
    public void findUsersByBirthDate_shouldReturnListOfUsers_WhereBirthDateIsMatching() {
        User user1 = User.builder().birthDate(LocalDate.of(1999, 4, 25)).build();
        User user2 = User.builder().birthDate(LocalDate.of(1990, 4, 25)).build();
        User user3 = User.builder().birthDate(LocalDate.of(2010, 4, 25)).build();
        User user4 = User.builder().birthDate(LocalDate.of(1998, 4, 25)).build();

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user4);

        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        repository.save(user4);

        List<User> actual = repository.findUsersByBirthDate(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2010, 3, 25)
        );

        assertEquals(expected, actual);
    }
}
