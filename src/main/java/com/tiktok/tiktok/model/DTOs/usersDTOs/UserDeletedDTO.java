package com.tiktok.tiktok.model.DTOs.usersDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserDeletedDTO {

    @Setter
    private int id;
    private String text = "Account successfully deleted";
}
