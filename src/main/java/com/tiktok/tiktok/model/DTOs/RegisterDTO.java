package com.tiktok.tiktok.model.DTOs;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class RegisterDTO {

    @Pattern(regexp = "^[A-Za-z -]{2,50}$", message = "Invalid name format")
    private String name;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+])(?=\\S+$).{8,}$", message = "Weak password")
    private String password;
    private String confirmPassword;
    private String confirmationCode;
    @Email(message = "Invalid email.")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phoneNumber;
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    @Size(min = 1, max = 1, message = "Gender must be a single character")
    @Pattern(regexp = "[mf]", message = "Gender must be either m or f")
    private String gender;
    @Size(max = 200, message = "The bio should not be larger than 200 symbols.")
    private String bio;
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only alphanumeric characters")
    private String username;
}
