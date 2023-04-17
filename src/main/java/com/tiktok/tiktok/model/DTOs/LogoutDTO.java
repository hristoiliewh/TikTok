package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@NoArgsConstructor
public class LogoutDTO {
    @Setter
    private int id;
    private String logoutText = "Successfully logged out";
}
