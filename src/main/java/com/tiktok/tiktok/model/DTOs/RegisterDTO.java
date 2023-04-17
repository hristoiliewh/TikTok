package com.tiktok.tiktok.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class RegisterDTO {
    private String name;
    private String password;
    private String confirmPassword;
    private String confirmationCode;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String bio;
    private String username;
}
