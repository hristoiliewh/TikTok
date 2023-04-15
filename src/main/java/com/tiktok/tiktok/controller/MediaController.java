package com.tiktok.tiktok.controller;



import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.model.DTOs.VideoSimpleDTO;
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.service.MediaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    @GetMapping("/media/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse resp){
        try {
            File f = mediaService.download(fileName);
            Files.copy(f.toPath(), resp.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException while downloading media");
        }
    }
}
