package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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