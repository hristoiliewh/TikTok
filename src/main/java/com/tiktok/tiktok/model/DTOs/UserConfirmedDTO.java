package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserConfirmedDTO {
    private int id;
    private String text = "Registration successfully confirmed!";
}
