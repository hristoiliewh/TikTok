package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<UserSimpleDTO> register(@RequestBody RegisterDTO dto) {

        UserSimpleDTO userSimpleDTO = userService.register(dto);
        return ResponseEntity.ok(userSimpleDTO);
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserFullInfoDTO> login(@RequestBody LoginDTO dto, HttpSession s) {
        UserFullInfoDTO u = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", u.getId());
        return ResponseEntity.ok(u);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<LogoutDTO> logout(HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        s.invalidate();
        LogoutDTO dto = new LogoutDTO();
        dto.setId(loggedUserId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/users/{followToId}/follow-unfollow")
    public ResponseEntity<Integer> follow(@PathVariable int followToId, HttpSession s) {
        int followerId = getLoggedUserId(s);
        int follow = userService.follow(followerId, followToId);
        return ResponseEntity.ok(follow);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserFullInfoDTO> searchByUsername(@PathVariable String username, HttpSession s) {
        UserFullInfoDTO userFullInfoDTO = userService.searchByUsername(username);
        return ResponseEntity.ok(userFullInfoDTO);
    }

    @GetMapping("/users/{id}/find")
    public ResponseEntity<UserFullInfoDTO> getById(@PathVariable int id) {

        UserFullInfoDTO userFullInfoDTO =  userService.getById(id);
        return ResponseEntity.ok(userFullInfoDTO);
    }

    @GetMapping("/users/{id}/followed")
    public ResponseEntity<List<UserWithPicNameIdDTO>> getAllFollowers(@PathVariable int id, HttpSession s) {
        isLogged(s);
        List<UserWithPicNameIdDTO> user = userService.getAllFollowers(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}/following")
    public ResponseEntity<List<UserWithPicNameIdDTO>> getAllFollowing(@PathVariable int id, HttpSession s) {
        isLogged(s);
        List<UserWithPicNameIdDTO> user = userService.getAllFollowing(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}/videos")
    public ResponseEntity<List<VideoWithoutOwnerDTO>> getAllVideos(@PathVariable int id, HttpSession s) {
        isLogged(s);
        List<VideoWithoutOwnerDTO> videoWithoutOwnerDTO =  userService.getAllVideos(id);
        return ResponseEntity.ok(videoWithoutOwnerDTO);
    }

    @DeleteMapping("/users")
    public ResponseEntity<UserDeletedDTO> deleteAccount(HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        UserDeletedDTO userDeletedDTO = userService.deleteAccount(loggedUserId);
        return ResponseEntity.ok(userDeletedDTO);
    }

    @PutMapping("/users/edit")
    public ResponseEntity<UserSimpleDTO> editAccount(@RequestBody UserEditDTO corrections, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        UserSimpleDTO dto = userService.editAccount(corrections, loggedUserId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/users/confirm-registration/{confirmationCode}")
    public ResponseEntity<UserConfirmedDTO> confirmRegistration(@PathVariable String confirmationCode) {
        UserConfirmedDTO userConfirmedDTO =  userService.confirmRegistration(confirmationCode);
        return ResponseEntity.ok(userConfirmedDTO);
    }

    @PostMapping("/users/forgot-password")
    public ResponseEntity<PasswordDTO> forgottenPassword(@RequestBody ForgottenPasswordDTO dto) {
        PasswordDTO passwordDTO = userService.forgottenPassword(dto);
        return ResponseEntity.ok(passwordDTO);
    }
}
