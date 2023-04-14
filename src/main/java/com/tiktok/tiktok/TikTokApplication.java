package com.tiktok.tiktok;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TikTokApplication {

    public static void main(String[] args) {
        SpringApplication.run(TikTokApplication.class, args);
    }

    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
