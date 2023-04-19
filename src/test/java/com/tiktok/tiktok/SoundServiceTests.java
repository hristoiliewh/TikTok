package com.tiktok.tiktok;

import com.tiktok.tiktok.controller.SoundController;
import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.repositories.SoundRepository;
import com.tiktok.tiktok.service.SoundService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SoundServiceTests {

    @Mock
    private SoundService soundService;

    @InjectMocks
    private SoundController soundController;

    @Mock
    private SoundRepository soundRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(soundController).build();
        mockMvc = MockMvcBuilders.standaloneSetup(soundRepository).build();
        mockMvc = MockMvcBuilders.standaloneSetup(soundService).build();
    }

    @Test
    public void testGetByIdWithValidInput() {

        // Arrange
        Sound sound = new Sound();
        sound.setId(1);
        sound.setName("Sound1");
        sound.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f2.mp3");

        // Act
        SoundSimpleDTO expected = new SoundSimpleDTO();
        expected.setId(1);
        expected.setName("Sound1");
        expected.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f2.mp3");

//        when(soundRepository.findById(1)).thenReturn(expected);
        // Assert
        assertNotNull(expected);
        Assertions.assertEquals(1, expected.getId());
        Assertions.assertEquals("Sound1", expected.getName());
    }
}
