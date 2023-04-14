package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    public UserSimpleDTO register(@RequestBody RegisterDTO dto){

        UserSimpleDTO u = userService.register(dto);
        return u;
    }
    @PostMapping("/users/login")
    public UserFullInfoDTO login(@RequestBody LoginDTO dto, HttpSession s){
        UserFullInfoDTO respDto = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", respDto.getId());
        return respDto;
    }
    @PostMapping("/users/logout")
    public LogoutDTO logout(HttpSession s){
        isLogged(s);
        s.invalidate();
        return new LogoutDTO();
    }
    @PostMapping("/users/{followToId}/follow")
    public int follow(@PathVariable int followToId, HttpSession s){
        int followerId = getLoggedUserId(s);
        return userService.follow(followerId, followToId);
    }
    @GetMapping("/users/{username}")
    public UserFullInfoDTO searchByUsername(@PathVariable String username, HttpSession s){
        return userService.searchByUsername(username);
    }
}
