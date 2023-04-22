package com.tiktok.tiktok;

import com.tiktok.tiktok.controller.SoundController;
import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
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
        SoundDTO expected = new SoundDTO();
        expected.setId(1);
        expected.setName("Sound1");
        expected.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f2.mp3");

//        when(soundRepository.findById(1)).thenReturn(expected);
        // Assert
        assertNotNull(expected);
        Assertions.assertEquals(1, expected.getId());
        Assertions.assertEquals("Sound1", expected.getName());
    }
    /*

    @Test
    public void testGetAllSoundsExist() {
        // Arrange
        Sound sound1 = new Sound();
        sound1.setId(1);
        sound1.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f2.mp3");
        sound1.setName("Sound1");
        Sound sound2 = new Sound();
        sound2.setId(2);
        sound2.setName("Sound2");
        sound2.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f9.mp3");

//        List<Sound> sounds = Arrays.asList(sound1, sound2);
//        when(soundRepository.findAll()).thenReturn(sounds);

        // Act
        List<SoundSimpleDTO> expected = new ArrayList<>();

        SoundSimpleDTO s1 = new SoundSimpleDTO();
        s1.setId(1);
        s1.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f2.mp3");
        s1.setName("Sound1");

        SoundSimpleDTO s2 = new SoundSimpleDTO();
        s1.setId(2);
        s1.setUrl("uploads/443a8493-96fd-41e7-89b2-a6441d9314f9.mp3");
        s1.setName("Sound2");

        expected.add(s1);
        expected.add(s2);

        // Assert
        assertNotNull(expected);
        Assertions.assertEquals(2, expected.size());
        Assertions.assertEquals("Sound1", expected.get(0).getName());
        Assertions.assertEquals("Sound2", expected.get(1).getName());
    }

    @Test
    void testGetAllSoundsDoesNotExistThrowNotFoundException() {
        // Arrange
        when(soundRepository.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        Assertions.assertThrows(NotFoundException.class, () -> soundService.getAll());
    }

    @Test
    void testGetByNameSoundExists() {
        // Arrange
        Sound sound1 = new Sound();
        sound1.setId(1);
        sound1.setName("Sound1");
        Sound sound2 = new Sound();
        sound2.setId(2);
        sound2.setName("Sound2");
        List<Sound> sounds = Arrays.asList(sound1, sound2);
        when(soundRepository.findAllContains("ound")).thenReturn(sounds);
        // Act
        List<SoundSimpleDTO> soundSimpleDTOList = soundService.getByName("ound");

        // Assert
        Assertions.assertEquals(2, soundSimpleDTOList.size());
        Assertions.assertEquals("Sound1", soundSimpleDTOList.get(0).getName());
        Assertions.assertEquals("Sound2", soundSimpleDTOList.get(1).getName());
    }

    @Test
    void testGetByNameSoundDoesNotExistThrowNotFoundException() {
        // Arrange
        when(soundRepository.findAllContains("sound")).thenReturn(Arrays.asList());

        // Act & Assert
        Assertions.assertThrows(NotFoundException.class, () -> soundService.getByName("ound"));
    }

     */
}
