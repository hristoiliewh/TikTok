package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.service.AbstractService;
import com.tiktok.tiktok.service.UserService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;
@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<UserSimpleDTO> register(@Valid @RequestBody RegisterDTO dto) {
        UserSimpleDTO userSimpleDTO = userService.register(dto);
        return ResponseEntity.ok(userSimpleDTO);
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserFullInfoDTO> login(@RequestBody LoginDTO dto, HttpSession s) {
        UserFullInfoDTO u = userService.login(dto);
        s.setAttribute("LOGGED_ID", u.getId());
        return ResponseEntity.ok(u);
    }

    @PostMapping("/users/logout")
    public void logout(HttpSession s) {
        isLogged(s);
        s.invalidate();
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
    @ApiOperation(value = "Get user by ID", nickname = "getUserById")
    @GetMapping("/users/{id}/find")
    public ResponseEntity<UserFullInfoDTO> getById(@PathVariable int id) {

        UserFullInfoDTO userFullInfoDTO =  userService.getById(id);
        return ResponseEntity.ok(userFullInfoDTO);
    }

    @GetMapping("/users/{id}/followed")
    public ResponseEntity<List<UserWithPicNameIdDTO>> getAllFollowers(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "4") int limit,
                                                                      @PathVariable int id, HttpSession s) {
        isLogged(s);
        List<UserWithPicNameIdDTO> user = userService.getAllFollowers(id, page, limit);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}/following")
    public ResponseEntity<List<UserWithPicNameIdDTO>> getAllFollowing(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "4") int limit,
                                                                      @PathVariable int id, HttpSession s) {
        isLogged(s);
        List<UserWithPicNameIdDTO> user = userService.getAllFollowing(id, page, limit);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users")
    public ResponseEntity<UserDeletedDTO> deleteAccount(HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        UserDeletedDTO userDeletedDTO = userService.deleteAccount(loggedUserId);
        return ResponseEntity.ok(userDeletedDTO);
    }

    @PutMapping("/users")
    public ResponseEntity<UserSimpleDTO> editAccount(@RequestBody UserEditDTO corrections, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        UserSimpleDTO dto = userService.editAccount(corrections, loggedUserId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/users/confirm-registration/{confirmationCode}")
    public ResponseEntity<UserConfirmedDTO> confirmRegistration(@PathVariable String confirmationCode) {
        UserConfirmedDTO userConfirmedDTO =  userService.confirmRegistration(confirmationCode);
        return ResponseEntity.ok(userConfirmedDTO);
    }

    @PostMapping("/users/forgot-password")
    public String forgottenPassword(@RequestBody ForgottenPasswordDTO dto) {
        return userService.forgottenPassword(dto);
    }
}
