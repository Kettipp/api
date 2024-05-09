package com.my.api.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@Validated
@Slf4j
public class User {
    private long id;
    @NotEmpty(message = "Email cannot be empty")
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate birthDate;
    private String address;
    @Pattern(regexp = "\\+\\d*", message = "Phone number must be numeric with +")
    private String phoneNumber;

    @AssertTrue(message = "User must be at least 18 years old")
    public boolean isAdult(@Value("${age}") int age) {
        return birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() >= age;
    }

    public void setBirthDate(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new InvalidParameterException("User must be at least 18 years old");
        } else {
            this.birthDate = birthDate;
        }
    }
}
