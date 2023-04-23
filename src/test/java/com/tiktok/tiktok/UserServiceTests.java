package com.tiktok.tiktok;

import com.tiktok.tiktok.model.DTOs.usersDTOs.*;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import com.tiktok.tiktok.model.repositories.UserRepository;
import com.tiktok.tiktok.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetByIdUserExists() {
        User user = new User();
        user.setId(1);
        UserFullInfoDTO userFullInfoDTO = new UserFullInfoDTO();
        userFullInfoDTO.setId(user.getId());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        when(mapper.map(user, UserFullInfoDTO.class)).thenReturn(userFullInfoDTO);

        UserFullInfoDTO result = userService.getById(1);

        assertEquals(userFullInfoDTO.getId(), result.getId());
        verify(userRepository, times(1)).findById(1);
        verify(mapper, times(1)).map(user, UserFullInfoDTO.class);
    }
    @Test
    public void testGetByIdUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(1));
    }
    @Test
    public void testLoginSuccess() {

        String username = "testuser";
        String password = "password";
        User user = new User();
        user.setId(1);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEmailConfirmed(true);
        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
        when(encoder.matches(password, user.getPassword())).thenReturn(true);

        UserFullInfoDTO userFullInfoDTO = new UserFullInfoDTO();
        userFullInfoDTO.setId(user.getId());
        userFullInfoDTO.setUsername(user.getUsername());
        when(mapper.map(user, UserFullInfoDTO.class)).thenReturn(userFullInfoDTO);

        LoginDTO loginData = generateLoginDTO();

        UserFullInfoDTO result = userService.login(loginData);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).getByUsername(username);
        verify(encoder, times(1)).matches(password, user.getPassword());
        verify(mapper, times(1)).map(user, UserFullInfoDTO.class);
    }

    @Test
    public void testWrongUsername() {
        LoginDTO loginData = generateLoginDTO();

        when(userRepository.getByUsername(loginData.getUsername())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> {
            userService.login(loginData);
        });
    }
    @Test
    public void testWrongPassword() {
        LoginDTO loginData = generateLoginDTO();

        User user = new User();
        user.setUsername(loginData.getUsername());
        user.setPassword(encoder.encode(loginData.getPassword()));

        lenient().when(encoder.matches(loginData.getPassword(), user.getPassword())).thenReturn(false);

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.login(loginData);
        });
    }
    private LoginDTO generateLoginDTO(){
        LoginDTO loginData = new LoginDTO();
        loginData.setUsername("testuser");
        loginData.setPassword("password");
        return loginData;
    }
}