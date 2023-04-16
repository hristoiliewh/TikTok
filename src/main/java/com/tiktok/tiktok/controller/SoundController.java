package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.service.SoundService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SoundController extends AbstractController {
    @Autowired
    private SoundService soundService;


    @GetMapping("/sounds/{soundId}/find")
    public SoundSimpleDTO getById(@PathVariable int soundId) {
        return soundService.getById(soundId);
    }
    @GetMapping("/sounds")
    public List<SoundSimpleDTO> getAll(HttpSession s) {
        isLogged(s);
        return soundService.getAll();
    }
    @GetMapping("/sounds/{soundName}")
    public List<SoundSimpleDTO> getByName(@PathVariable String soundName) {
        return soundService.getByName(soundName);
    }
}
