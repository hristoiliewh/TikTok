package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.usersDTOs.*;
import com.tiktok.tiktok.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<List<UserWithPicNameIdDTO>> searchByUsername(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "1") int limit,
                                                            @PathVariable String username, HttpSession s) {
        List<UserWithPicNameIdDTO> userFullInfoDTO = userService.searchByUsername(username, page, limit);
        return ResponseEntity.ok(userFullInfoDTO);
    }

    @GetMapping("/users/{id}/find")
    public ResponseEntity<UserFullInfoDTO> getById(@PathVariable int id) {

        UserFullInfoDTO userFullInfoDTO =  userService.getById(id);
        return ResponseEntity.ok(userFullInfoDTO);
    }

    @GetMapping("/users/{id}/followed")
    public ResponseEntity<Page<UserWithPicNameIdDTO>> getAllFollowers(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "4") int limit,
                                                                      @PathVariable int id, HttpSession s) {
        isLogged(s);
        Page<UserWithPicNameIdDTO> user = userService.getAllFollowers(id, page, limit);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}/following")
    public ResponseEntity<Page<UserWithPicNameIdDTO>> getAllFollowing(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "4") int limit,
                                                                      @PathVariable int id, HttpSession s) {
        isLogged(s);
        Page<UserWithPicNameIdDTO> user = userService.getAllFollowing(id, page, limit);
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
