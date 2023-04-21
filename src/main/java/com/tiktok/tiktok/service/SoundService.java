package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoundService extends AbstractService {


    public SoundSimpleDTO getById(int soundId) {
        Sound sound = getSoundById(soundId);
        return mapper.map(sound, SoundSimpleDTO.class);
    }

    public List<SoundSimpleDTO> getAll(int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Sound> sounds = soundRepository.findAll(pageable);
        if (sounds.getContent().size() == 0) {
            throw new NotFoundException("No sound found");
        }
        return sounds.stream()
                .map(sound -> mapper.map(sound, SoundSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public List<SoundSimpleDTO> getByName(String soundName, int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Sound> sounds = soundRepository.findAllContains(soundName, pageable);
        if (sounds.getContent().size() == 0) {
            throw new NotFoundException("Sound not found");
        }
        return sounds.stream()
                .map(s -> mapper.map(s, SoundSimpleDTO.class))
                .collect(Collectors.toList());
    }
}
