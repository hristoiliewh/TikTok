package com.tiktok.tiktok;

import com.tiktok.tiktok.service.MailSenderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TikTokApplication {

    @Autowired
    private MailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(TikTokApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail(){
//        senderService.sendEmail();
//    }


}
