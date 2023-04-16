package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
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
    public VideoSimpleDTO getById(@PathVariable int videoId, HttpSession s){
        int userId = checkIfIsLogged(s);
        return videoService.getById(videoId, userId);
    }
    @GetMapping("/videos/{videoId}/comments")
    public List<CommentWithoutVideoDTO> getAllComments(@PathVariable int videoId, HttpSession s){
        int userId = checkIfIsLogged(s);
        return videoService.getAllComments(videoId, userId);
    }

    @PostMapping("/videos/{videoId}/comment")
    public CommentSimpleDTO addComment(@PathVariable int videoId, @RequestBody String text, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return videoService.addComment(videoId, loggedUserId, text);
    }

    @GetMapping("/videos/{videoName}/find")
    public List<VideoSimpleDTO> getByName(@PathVariable String videoName, HttpSession s) {
        int userId = checkIfIsLogged(s);
        return videoService.getByName(videoName, userId);
    }

    @GetMapping("/videos/hashtag/{hashtag}")
    public List<VideoSimpleDTO> getByHashtag(@PathVariable String hashtag, HttpSession s) {
        int userId = checkIfIsLogged(s);
        return videoService.getByHashtag(hashtag, userId);
    }

    @PostMapping("/videos/{videoId}/react")
    public VideoReactionDTO likeDislike(@PathVariable int videoId, HttpSession s){
        int userId = getLoggedUserId(s);
        return videoService.likeDislike(videoId, userId);
    }


}
