package com.tiktok.tiktok.model.DTOs.usersDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithPicNameIdDTO {
    private int id;
    private String name;
    private String username;
    private String profilePhotoURL;
}
