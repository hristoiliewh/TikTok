package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoundService extends AbstractService {

    public SoundDTO getById(int soundId) {
        Sound sound = soundRepository.findById(soundId).orElseThrow(() -> new NotFoundException("Sound not found"));
        return mapper.map(sound, SoundDTO.class);
    }

    public List<SoundDTO> getAll(int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Sound> sounds = soundRepository.findAll(pageable);
        if (sounds.getContent().size() == 0) {
            throw new NotFoundException("No sound found");
        }
        return sounds.stream()
                .map(sound -> mapper.map(sound, SoundDTO.class))
                .collect(Collectors.toList());
    }

    public List<SoundDTO> getByName(String soundName, int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Sound> sounds = soundRepository.findAllContains(soundName, pageable);
        if (sounds.getContent().size() == 0) {
            throw new NotFoundException("Sound not found");
        }
        return sounds.stream()
                .map(s -> mapper.map(s, SoundDTO.class))
                .collect(Collectors.toList());
    }
}
