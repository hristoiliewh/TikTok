package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.videosDTOs.*;
import com.tiktok.tiktok.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/users/{id}/videos")
    public ResponseEntity<Page<VideoWithoutOwnerDTO>> getAllVideos(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "1") int limit,
                                                                   @PathVariable int id, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        Page<VideoWithoutOwnerDTO> videoWithoutOwnerDTO =  videoService.getAllVideos(id, loggedUserId, page,limit);
        return ResponseEntity.ok(videoWithoutOwnerDTO);
    }

    @GetMapping("/videos/{videoName}/find")
    public ResponseEntity<Page<VideoSimpleDTO>> getByName(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "1") int limit,
                                                          @PathVariable String videoName, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        Page<VideoSimpleDTO> videoSimpleDTOS = videoService.getByName(videoName, loggedUserId, page, limit);
        return ResponseEntity.ok(videoSimpleDTOS);
    }

    @GetMapping("/videos/my-reaction")
    public ResponseEntity<Page<VideoSimpleDTO>> getMyLikedVideos(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "1") int limit,
                                                               HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        Page<VideoSimpleDTO> videoSimpleDTOS = videoService.getMyLikedVideos(loggedUserId, page, limit);
        return ResponseEntity.ok(videoSimpleDTOS);
    }

    @GetMapping("/videos/hashtag/{hashtag}")
    public ResponseEntity<Page<VideoSimpleDTO>> getByHashtag(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "1") int limit,
                                                             @PathVariable String hashtag, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        Page<VideoSimpleDTO> videoSimpleDTOS = videoService.getByHashtag(hashtag, loggedUserId, page, limit);
        return ResponseEntity.ok(videoSimpleDTOS);
    }

    @PostMapping("/videos/{videoId}/react")
    public ResponseEntity<VideoReactionDTO> likeDislike(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        VideoReactionDTO videoReactionDTO = videoService.likeDislike(videoId, loggedUserId);
        return ResponseEntity.ok(videoReactionDTO);
    }

    @GetMapping("/videos/{videoId}/reactions")
    public int getReactions(@PathVariable int videoId, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        return videoService.getReactions(videoId, loggedUserId);
    }

    @GetMapping("/videos/homePage")
    public ResponseEntity<Page<VideoSimpleDTO>> showFeed(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "1") int limit, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        return ResponseEntity.ok(videoService.showFeed(loggedUserId, page, limit));
    }
}
