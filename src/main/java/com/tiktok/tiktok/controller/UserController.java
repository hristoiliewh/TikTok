package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        UserFullInfoDTO u = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", u.getId());
        return u;
    }
    @PostMapping("/users/logout")
    public LogoutDTO logout(HttpSession s){
        isLogged(s);
        s.invalidate();
        return new LogoutDTO();
    }
    @PostMapping("/users/{followToId}/follow-unfollow")
    public int follow(@PathVariable int followToId, HttpSession s){
        int followerId = getLoggedUserId(s);
        return userService.follow(followerId, followToId);
    }
    @GetMapping("/users/{username}")
    public UserFullInfoDTO searchByUsername(@PathVariable String username, HttpSession s){
        return userService.searchByUsername(username);
    }
    @GetMapping("/users/{id}/find")
    public UserFullInfoDTO getById(@PathVariable int id){
        System.out.println("Start searching");
        return userService.getById(id);
    }
    @GetMapping("/users/{id}/followed")
    public List<UserWithPicNameIdDTO> getAllFollowers(@PathVariable int id, HttpSession s){
        isLogged(s);
        return userService.getAllFollowers(id);
    }
    @GetMapping("/users/{id}/following")
    public List<UserWithPicNameIdDTO> getAllFollowing(@PathVariable int id, HttpSession s){
        isLogged(s);
        return userService.getAllFollowing(id);
    }
    @GetMapping("/users/{id}/videos")
    public List<VideoWithoutOwnerDTO> getAllVideos(@PathVariable int id, HttpSession s){
        isLogged(s);
        return userService.getAllVideos(id);
    }
    @DeleteMapping("/users")
    public UserDeletedDTO deleteAccount(HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return userService.deleteAccount(loggedUserId);
    }
    @PutMapping("/users/edit")
    public UserSimpleDTO editAccount(@RequestBody UserEditDTO corrections, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        System.out.println("nasdkjbasbfkjbskj;dsa");
        return userService.editAccount(corrections, loggedUserId);
    }
    @PutMapping("/users/{id}/confirm-registration")
    public UserConfirmedDTO confirmRegistration(@PathVariable int id, @RequestBody ConfirmDTO dto){
        return userService.confirmRegistration(id, dto);
    }
    @PostMapping("/users/forgot-password")
    public PasswordDTO forgottenPassword(@RequestBody ForgottenPasswordDTO dto){
        return userService.forgottenPassword(dto);
    }
}
