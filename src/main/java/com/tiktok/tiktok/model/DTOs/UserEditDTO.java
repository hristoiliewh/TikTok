package com.tiktok.tiktok.model.DTOs;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

@Setter
@Getter
@NoArgsConstructor
public class UserEditDTO {
    private String name;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String bio;
    private String username;
    private String email;
}