package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private VideoService videoService;

}
