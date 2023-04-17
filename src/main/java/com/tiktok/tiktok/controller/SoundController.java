package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.service.SoundService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SoundController extends AbstractController {
    @Autowired
    private SoundService soundService;


    @GetMapping("/sounds/{soundId}/find")
    public ResponseEntity<SoundSimpleDTO> getById(@PathVariable int soundId) {
        SoundSimpleDTO soundSimpleDTO = soundService.getById(soundId);
        return ResponseEntity.ok(soundSimpleDTO);
    }

    @GetMapping("/sounds")
    public ResponseEntity<List<SoundSimpleDTO>> getAll(HttpSession s) {
        isLogged(s);
        List<SoundSimpleDTO> soundSimpleDTOS = soundService.getAll();
        return ResponseEntity.ok(soundSimpleDTOS);
    }

    @GetMapping("/sounds/{soundName}")
    public ResponseEntity<List<SoundSimpleDTO>> getByName(@PathVariable String soundName) {

        List<SoundSimpleDTO> soundSimpleDTOS = soundService.getByName(soundName);
        return ResponseEntity.ok(soundSimpleDTOS);
    }
}
