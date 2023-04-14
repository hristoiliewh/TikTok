package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserWithoutPassDTO {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String bio;
    private String profilePhotoURL;
    private String username;
    private boolean isEmailConfirmed;
}
