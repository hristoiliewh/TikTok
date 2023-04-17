package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController extends AbstractController {
    @Autowired
    private VideoService videoService;

    @DeleteMapping("/videos/{videoId}")
    public ResponseEntity<VideoDeletedDTO> deleteVideo(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        VideoDeletedDTO videoDeletedDTO = videoService.deleteVideo(videoId, loggedUserId);
        return ResponseEntity.ok(videoDeletedDTO);
    }

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<VideoSimpleDTO> getById(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        VideoSimpleDTO videoSimpleDTO = videoService.getById(videoId, loggedUserId);
        return ResponseEntity.ok(videoSimpleDTO);
    }

    @GetMapping("/videos/{videoId}/comments")
    public ResponseEntity<List<CommentWithoutVideoDTO>> getAllComments(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        List<CommentWithoutVideoDTO> commentWithoutVideoDTOS = videoService.getAllComments(videoId, loggedUserId);
        return ResponseEntity.ok(commentWithoutVideoDTOS);
    }

    @PostMapping("/videos/{videoId}/comment")
    public ResponseEntity<CommentFullInfoDTO> addComment(@PathVariable int videoId, @RequestBody String comment, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentFullInfoDTO commentFullInfoDTO = videoService.addComment(videoId, loggedUserId, comment);
        return ResponseEntity.ok(commentFullInfoDTO);
    }

    @GetMapping("/videos/{videoName}/find")
    public ResponseEntity<List<VideoSimpleDTO>> getByName(@PathVariable String videoName, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        List<VideoSimpleDTO> videoSimpleDTOS = videoService.getByName(videoName, loggedUserId);
        return ResponseEntity.ok(videoSimpleDTOS);
    }

    @GetMapping("/videos/hashtag/{hashtag}")
    public ResponseEntity<List<VideoSimpleDTO>> getByHashtag(@PathVariable String hashtag, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        List<VideoSimpleDTO> videoSimpleDTOS = videoService.getByHashtag(hashtag, loggedUserId);
        return ResponseEntity.ok(videoSimpleDTOS);
    }

    @PostMapping("/videos/{videoId}/react")
    public ResponseEntity<VideoReactionDTO> likeDislike(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        VideoReactionDTO videoReactionDTO = videoService.likeDislike(videoId, loggedUserId);
        return ResponseEntity.ok(videoReactionDTO);
    }

    @GetMapping("/videos/{videoId}/reactions")
    public ResponseEntity<NumberOfReactionsDTO> getReactions(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        NumberOfReactionsDTO numberOfReactionsDTO = videoService.getReactions(videoId, loggedUserId);
        return ResponseEntity.ok(numberOfReactionsDTO);
    }
}
