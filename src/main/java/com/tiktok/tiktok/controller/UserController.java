package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.LoginDTO;
import com.tiktok.tiktok.model.DTOs.RegisterDTO;
import com.tiktok.tiktok.model.DTOs.UserWithoutPassDTO;
import com.tiktok.tiktok.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    public UserWithoutPassDTO register(@RequestBody RegisterDTO dto){

        UserWithoutPassDTO u = userService.register(dto);
        return u;
    }
    @PostMapping("/users/login")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s){
        UserWithoutPassDTO respDto = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", respDto.getId());
        return respDto;
    }
}
