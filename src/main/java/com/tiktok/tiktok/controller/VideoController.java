package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private VideoService videoService;
    @DeleteMapping("/videos/{videoId}")
    public DeleteVideoDTO deleteVideo(@PathVariable int videoId, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return videoService.deleteVideo(videoId, loggedUserId);
    }

}
