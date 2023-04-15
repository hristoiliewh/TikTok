package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.CommentSimpleDTO;
import com.tiktok.tiktok.model.DTOs.CommentWithoutVideoDTO;
import com.tiktok.tiktok.model.DTOs.VideoDeletedDTO;
import com.tiktok.tiktok.model.DTOs.VideoSimpleDTO;
import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private VideoService videoService;
    @DeleteMapping("/videos/{videoId}")
    public VideoDeletedDTO deleteVideo(@PathVariable int videoId, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return videoService.deleteVideo(videoId, loggedUserId);
    }

    @GetMapping("/videos/{videoId}")
    public VideoSimpleDTO getById(@PathVariable int videoId){
        return videoService.getById(videoId);
    }
    @GetMapping("/videos/{videoId}/comments")
    public List<CommentWithoutVideoDTO> getAllComments(@PathVariable int videoId){
        return videoService.getAllComments(videoId);
    }

    @PostMapping("/videos/{videoId}/comment")
    public CommentSimpleDTO addComment(@PathVariable int videoId, @RequestBody String text, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return videoService.addComment(videoId, loggedUserId, text);
    }

}
