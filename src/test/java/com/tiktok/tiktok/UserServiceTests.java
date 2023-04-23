//package com.tiktok.tiktok;
//import com.tiktok.tiktok.controller.UserController;
//import com.tiktok.tiktok.model.DTOs.usersDTOs.*;
//import com.tiktok.tiktok.model.entities.User;
//import com.tiktok.tiktok.model.exceptions.BadRequestException;
//import com.tiktok.tiktok.model.exceptions.NotFoundException;
//import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
//import com.tiktok.tiktok.model.repositories.UserRepository;
//import com.tiktok.tiktok.service.MailSenderService;
//import com.tiktok.tiktok.service.UserService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.*;
//import static org.mockito.Mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTests {
//
//    @Mock
//    private UserController userController;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private BCryptPasswordEncoder encoder;
//    @Mock
//    private ModelMapper mapper;
//
//    @InjectMocks
//    private UserService userService;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//        userService = mock(UserService.class);
//        userRepository = mock(UserRepository.class);
//        encoder = mock(BCryptPasswordEncoder.class);
//        mapper = mock(ModelMapper.class);
//        // other setup code...
//    }
//
//    private RegisterDTO getValidRegisterData() {
//        RegisterDTO registerData = new RegisterDTO();
//        registerData.setEmail("test@example.com");
//        registerData.setUsername("testuser");
//        registerData.setPassword("123aaaA$");
//        registerData.setConfirmPassword("123aaaA$");
//        registerData.setGender("m");
//        registerData.setName("Test test");
//        registerData.setPhoneNumber("0999123456");
//        return registerData;
//    }
//
//    @Test
//    public void testRegisterPasswordsNotMatching() {
//        // Creating a user register DTO with passwords that don't match
//        RegisterDTO registerData = getValidRegisterData();
//        registerData.setConfirmPassword("notMatchingPassword");
//        // Calling the register method should throw a BadRequestException
//        userService.register(registerData);
//    }
//
//    @Test
//    public void testRegisterEmailAlreadyExists() {
//        // Mocking the repository to return true when checking if the email already exists
//        when(userRepository.existsByEmail(anyString())).thenReturn(true);
//        // Creating a user register DTO with a strong password
//        RegisterDTO registerData = getValidRegisterData();
//        // Calling the register method should throw a BadRequestException
//        userService.register(registerData);
//    }
//
//    @Test
//    public void testLoginSuccess() {
//        // Arrange
//        String username = "testuser";
//        String password = "password";
//        User user = new User();
//        user.setId(1);
//        user.setUsername(username);
//        user.setPassword(encoder.encode(password));
//        user.setEmailConfirmed(true);
//        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
//        when(encoder.matches(password, user.getPassword())).thenReturn(true);
//
//        UserFullInfoDTO userFullInfoDTO = new UserFullInfoDTO();
//        userFullInfoDTO.setId(user.getId());
//        userFullInfoDTO.setUsername(user.getUsername());
//        when(mapper.map(user, UserFullInfoDTO.class)).thenReturn(userFullInfoDTO);
//
//        LoginDTO loginData = new LoginDTO();
//        loginData.setUsername(username);
//        loginData.setPassword(password);
//        // Act
//        UserFullInfoDTO result = userService.login(loginData);
//        // Assert
//        assertEquals(user.getId(), result.getId());
//        assertEquals(user.getUsername(), result.getUsername());
//        verify(userRepository, times(1)).getByUsername(username);
//        verify(encoder, times(1)).matches(password, user.getPassword());
//        verify(mapper, times(1)).map(user, UserFullInfoDTO.class);
//    }
//    @Test
//    public void testLoginUserNotFound() {
//        // Arrange
//        String username = "testuser";
//        LoginDTO loginData = new LoginDTO();
//        loginData.setUsername(username);
//        when(userRepository.getByUsername(username)).thenReturn(Optional.empty());
//        // Act
//        userService.login(loginData);
//    }
//    @Test
//    public void testLoginUnauthorized() {
//        // Arrange
//        String username = "testuser";
//        String password = "password";
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(encoder.encode("wrong_password"));
//        when(userRepository.getByUsername(username)).thenReturn(Optional.of(user));
//        when(encoder.matches(password, user.getPassword())).thenReturn(false);
//
//        LoginDTO loginData = new LoginDTO();
//        loginData.setUsername(username);
//        loginData.setPassword(password);
//        // Act
//        userService.login(loginData);
//    }
//    @Test
//    public void testEditSuccess() {
//        // Arrange
//        int userId = 1;
//        String newPassword = "New_password1!";
//        String confirmNewPassword = "New_password1!";
//        UserEditDTO userEditDTO = new UserEditDTO();
//        userEditDTO.setPassword(newPassword);
//        userEditDTO.setConfirmPassword(confirmNewPassword);
//        userEditDTO.setBio("");
//        userEditDTO.setEmail("");
//        userEditDTO.setPhoneNumber("");
//        userEditDTO.setName("");
//        userEditDTO.setUsername("");
//
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(encoder.encode(newPassword)).thenReturn("encoded_new_password");
//
//        User updatedUser = new User();
//        updatedUser.setId(userId);
//        updatedUser.setPassword("encoded_new_password");
//        when(userRepository.save(user)).thenReturn(updatedUser);
//
//        UserSimpleDTO expected = new UserSimpleDTO();
//        expected.setId(userId);
//        expected.setUsername(user.getUsername());
//        when(mapper.map(updatedUser, UserSimpleDTO.class)).thenReturn(expected);
//        // Act
//        UserSimpleDTO result = userService.editAccount(userEditDTO, userId);
//        // Assert
//        assertEquals(expected.getId(), result.getId());
//        assertEquals(expected.getUsername(), result.getUsername());
//    }
//    @Test
//    public void testChangePassMismatchedPassword() {
//        // Arrange
//        int userId = 1;
//        String newPassword = "new_password";
//        String confirmNewPassword = "wrong_password";
//        UserEditDTO changePassDTO = new UserEditDTO();
//        changePassDTO.setPassword(newPassword);
//        changePassDTO.setConfirmPassword(confirmNewPassword);
//        // Act
//        userService.editAccount(changePassDTO, userId);
//    }
//}
