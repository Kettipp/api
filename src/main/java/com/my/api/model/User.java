package com.my.api.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@Validated
public class User {
    private long id;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    @AssertTrue(message = "User must be at least 18 years old")
    public boolean isAdult(@Value("${age}") int age) {
        return birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() >= age;
    }
}
