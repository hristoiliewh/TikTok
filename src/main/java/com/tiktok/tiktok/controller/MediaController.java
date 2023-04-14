package com.tiktok.tiktok.controller;

<<<<<<< HEAD
import com.tiktok.tiktok.model.DTOs.UserWithoutPassDTO;
import com.tiktok.tiktok.model.entities.Video;
=======
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
>>>>>>> cdc216b247d57d29cde4b2f0d5f51c76de114135
import com.tiktok.tiktok.service.MediaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
public class MediaController extends AbstractController{

    @Autowired
    private MediaService mediaService;

    @PostMapping("/media")

    public UserSimpleDTO upload(@RequestParam("file") MultipartFile file, HttpSession s){

        int id = getLoggedUserId(s);
        return mediaService.upload(file, id);
    }
    @PostMapping("/videos/media")
    public Video uploadVideo(@RequestParam(value = "file") MultipartFile file,
                             @RequestParam(value = "caption") String caption,
                             @RequestParam(value = "isPrivate") Boolean isPrivate,
                             @RequestParam(value = "soundId") int soundId,
                             HttpSession s) {
        int ownerId = getLoggedUserId(s);
        return mediaService.uploadVideo(ownerId, file, caption, isPrivate, soundId);
    }
}
