package com.tiktok.tiktok.controller;



import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.model.DTOs.VideoSimpleDTO;
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
import com.tiktok.tiktok.model.entities.Sound;
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

    public UserSimpleDTO upload(@RequestParam("file") MultipartFile file, HttpSession s){

        int id = getLoggedUserId(s);
        return mediaService.upload(file, id);
    }
    @PostMapping("/videos/media")
    public VideoSimpleDTO uploadVideo(@RequestParam("file") MultipartFile file,
                                      @RequestParam("caption") String caption,
                                      @RequestParam("isPrivate") Boolean isPrivate,
                                      @RequestParam("soundId") int soundId,
                                      HttpSession s) {
        int ownerId = getLoggedUserId(s);
        return mediaService.uploadVideo(ownerId, file, caption, isPrivate, soundId);
    }

    @PostMapping("/sounds")
    public SoundSimpleDTO uploadSound(@RequestParam("file") MultipartFile file,
                                      @RequestParam("name") String name,
                                      HttpSession s) {
        isLogged(s);
        return mediaService.uploadSound(file, name);
    }
}
