package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
import com.tiktok.tiktok.service.SoundService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class SoundController extends AbstractController {
    @Autowired
    private SoundService soundService;

    @GetMapping("/sounds/{soundId}/find")
    public ResponseEntity<SoundDTO> getById(@PathVariable int soundId) {
        SoundDTO soundSimpleDTO = soundService.getById(soundId);
        return ResponseEntity.ok(soundSimpleDTO);
    }

    @GetMapping("/sounds")
    public ResponseEntity<Page<SoundDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "2") int limit, HttpSession s) {
        isLogged(s);
        Page<SoundDTO> soundSimpleDTOS = soundService.getAll(page, limit);
        return ResponseEntity.ok(soundSimpleDTOS);
    }

    @GetMapping("/sounds/{soundName}")
    public ResponseEntity<Page<SoundDTO>> getByName(@PathVariable String soundName, @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "2") int limit) {

        Page<SoundDTO> soundSimpleDTOS = soundService.getByName(soundName, page, limit);
        return ResponseEntity.ok(soundSimpleDTOS);
    }
}
