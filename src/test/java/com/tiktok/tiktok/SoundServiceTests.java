package com.tiktok.tiktok;

import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.repositories.SoundRepository;
import com.tiktok.tiktok.service.SoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundServiceTests {
    @InjectMocks
    private SoundService soundService;
    @Mock
    private SoundRepository soundRepository;
    @Mock
    private ModelMapper mapper;
    private SoundDTO soundDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        soundDTO = new SoundDTO();
        soundDTO.setId(1);
        soundDTO.setName("Test Sound");
        soundDTO.setUrl("uploads/809b6510-65af-41fd-8bdc-e0ba606fec72.mp3");
    }

    @Test
    public void testGetByIdSoundExists() {
        Sound sound = new Sound();
        sound.setId(1);

        when(soundRepository.findById(1)).thenReturn(Optional.of(sound));

        when(mapper.map(sound, SoundDTO.class)).thenReturn(soundDTO);

        SoundDTO result = soundService.getById(1);

        assertEquals(soundDTO.getId(), result.getId());
        verify(soundRepository, times(1)).findById(1);
        verify(mapper, times(1)).map(sound, SoundDTO.class);
    }

    @Test
    public void testGetByIdSoundNotFound() {
        when(soundRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> soundService.getById(1));
    }

    @Test
    public void testGetAllSoundsFound() {
        when(soundRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(new Sound())));

        when(mapper.map(any(Sound.class), eq(SoundDTO.class))).thenReturn(soundDTO);

        Page<SoundDTO> result = soundService.getAll(0, 10);
        assertEquals(1, result.getContent().size());
        assertEquals(soundDTO, result.getContent().get(0));
    }

    @Test
    public void testGetAllSoundsNotFound() {
        when(soundRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> soundService.getAll(0, 10));
    }
}