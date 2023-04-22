package com.tiktok.tiktok.model.DTOs.usersDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserSimpleDTO {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String bio;
    private String profilePhotoURL;
    private String username;
}
