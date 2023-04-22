package com.tiktok.tiktok;
import com.tiktok.tiktok.controller.UserController;
import com.tiktok.tiktok.model.DTOs.usersDTOs.LoginDTO;
import com.tiktok.tiktok.model.DTOs.usersDTOs.RegisterDTO;
import com.tiktok.tiktok.model.DTOs.usersDTOs.UserFullInfoDTO;
import com.tiktok.tiktok.model.DTOs.usersDTOs.UserSimpleDTO;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.repositories.UserRepository;
import com.tiktok.tiktok.service.MailSenderService;
import com.tiktok.tiktok.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikTokApplicationTests.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private ModelMapper mapper;

    @Mock
    private MailSenderService senderService = mock(MailSenderService.class);

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // configure any required mock behavior
        mockMvc = MockMvcBuilders.standaloneSetup(userRepository).build();
//        MockitoAnnotations.openMocks(this);
    }

    private RegisterDTO getValidRegisterData() {
        RegisterDTO registerData = new RegisterDTO();
        registerData.setEmail("test@example.com");
        registerData.setUsername("testuser");
        registerData.setPassword("123aaaA$");
        registerData.setConfirmPassword("123aaaA$");
        registerData.setGender("m");
        registerData.setName("Test test");
        registerData.setPhoneNumber("0999123456");
        return registerData;
    }

    @org.junit.Test(expected = BadRequestException.class)
    public void testRegisterPasswordsNotMatching() {
        // Creating a user register DTO with passwords that don't match
        RegisterDTO registerData = getValidRegisterData();
        registerData.setConfirmPassword("notMatchingPassword");
        // Calling the register method should throw a BadRequestException
        userService.register(registerData);
    }

    @org.junit.Test(expected = BadRequestException.class)
    public void testRegisterEmailAlreadyExists() {
        // Mocking the repository to return true when checking if the email already exists
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // Creating a user register DTO with a strong password
        RegisterDTO registerData = getValidRegisterData();
        // Calling the register method should throw a BadRequestException
        userService.register(registerData);
    }

    @org.junit.Test
    public void testLoginSuccess() {
        // Arrange
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

        LoginDTO loginData = new LoginDTO();
        loginData.setUsername(username);
        loginData.setPassword(password);
        // Act
        UserFullInfoDTO result = userService.login(loginData);
        // Assert
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).getByUsername(username);
        verify(encoder, times(1)).matches(password, user.getPassword());
        verify(mapper, times(1)).map(user, UserFullInfoDTO.class);
    }
}
