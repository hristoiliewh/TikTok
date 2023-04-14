package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.UserWithoutPassDTO;
import com.tiktok.tiktok.service.MediaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MediaController extends AbstractController{

    @Autowired
    private MediaService mediaService;

    @PostMapping("/media")
    public UserWithoutPassDTO upload(@RequestParam("file") MultipartFile file, HttpSession s){
        int id = getLoggedUserId(s);
        return mediaService.upload(file, id);
    }
}
