package com.my.api;

import com.my.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserValidationTest {
    private static final int AGE = 18;
    @Test
    public void isAdult_shouldReturnsFalse_WhenAgeIsUnder18() {
        LocalDate lessThanEighteenYearsAgo = LocalDate.now().minusYears(18).plusDays(1);
        User user = User.builder().birthDate(lessThanEighteenYearsAgo).build();

        assertFalse(user.isAdult(AGE), "User must be at least 18 years old");
    }

    @Test
    void isAdult_shouldReturnsTrue_WhenAgeIsOver18() {
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18).minusDays(1);
        User user = User.builder().birthDate(eighteenYearsAgo).build();

        assertTrue(user.isAdult(AGE));
    }

    @Test
    void isAdult_shouldReturnsTrue_WhenAgeIsExactly18() {
        LocalDate eighteenYearsAgoToday = LocalDate.now().minusYears(18);
        User user = User.builder().birthDate(eighteenYearsAgoToday).build();

        assertTrue(user.isAdult(AGE));
    }
}
