package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class PasswordDTO {
    private String text = "The password reset email has been sent successfully.";
}
