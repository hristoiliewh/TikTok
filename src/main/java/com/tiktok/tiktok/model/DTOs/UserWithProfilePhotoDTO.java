package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserWithProfilePhotoDTO extends RegisterDTO{

    private String profilePhotoURL;
}
