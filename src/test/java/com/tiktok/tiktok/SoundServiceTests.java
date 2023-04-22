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
import java.util.Collections;
import java.util.List;
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
        // Mocking the soundRepository to return a Sound object when getById is called
        when(soundRepository.findById(1)).thenReturn(Optional.of(sound));
        // Mocking the mapper to return a SoundDTO object when mapping the Sound object
        when(mapper.map(sound, SoundDTO.class)).thenReturn(soundDTO);
        // Act
        SoundDTO result = soundService.getById(1);
        // Assert
        assertEquals(soundDTO.getId(), result.getId());
        verify(soundRepository, times(1)).findById(1);
        verify(mapper, times(1)).map(sound, SoundDTO.class);
    }

    @Test
    public void testGetByIdSoundNotFound() {
        // Mocking the soundRepository to return an empty optional when getById is called
        when(soundRepository.findById(anyInt())).thenReturn(Optional.empty());
        // Calling getById should throw a NotFoundException
        assertThrows(NotFoundException.class, () -> soundService.getById(1));
    }

    @Test
    public void testGetAllSoundsFound() {
        // Mocking the soundRepository to return a Page object with one Sound object when findAll is called
        when(soundRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(new Sound())));
        // Mocking the mapper to return a SoundDTO object when mapping the Sound object
        when(mapper.map(any(Sound.class), eq(SoundDTO.class))).thenReturn(soundDTO);
        // Calling getAll should return a list with one mapped SoundDTO object
        Page<SoundDTO> result = soundService.getAll(0, 10);
        assertEquals(1, result.getContent().size());
        assertEquals(soundDTO, result.getContent().get(0));
    }

    @Test
    public void testGetAllSoundsNotFound() {
        // Mocking the soundRepository to return an empty Page object when findAll is called
        when(soundRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());
        // Calling getAll should throw a NotFoundException
        assertThrows(NotFoundException.class, () -> soundService.getAll(0, 10));
    }
}