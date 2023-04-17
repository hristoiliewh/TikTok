package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController extends AbstractController {
    @Autowired
    private VideoService videoService;

    @DeleteMapping("/videos/{videoId}")
    public VideoDeletedDTO deleteVideo(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        return videoService.deleteVideo(videoId, loggedUserId);
    }

    @GetMapping("/videos/{videoId}")
    public VideoSimpleDTO getById(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getById(videoId, loggedUserId);
    }

    @GetMapping("/videos/{videoId}/comments")
    public List<CommentWithoutVideoDTO> getAllComments(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getAllComments(videoId, loggedUserId);
    }

    @PostMapping("/videos/{videoId}/comment")
    public CommentSimpleDTO addComment(@PathVariable int videoId, @RequestBody String comment, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        return videoService.addComment(videoId, loggedUserId, comment);
    }

    @GetMapping("/videos/{videoName}/find")
    public List<VideoSimpleDTO> getByName(@PathVariable String videoName, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getByName(videoName, loggedUserId);
    }

    @GetMapping("/videos/hashtag/{hashtag}")
    public List<VideoSimpleDTO> getByHashtag(@PathVariable String hashtag, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getByHashtag(hashtag, loggedUserId);
    }

    @PostMapping("/videos/{videoId}/react")
    public VideoReactionDTO likeDislike(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        return videoService.likeDislike(videoId, loggedUserId);
    }

    @GetMapping("/videos/{videoId}/reactions")
    public NumberOfReactionsDTO getReactions(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getReactions(videoId, loggedUserId);
    }
}
