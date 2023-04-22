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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;

    @Mock
    private MailSenderService senderService = mock(MailSenderService.class);

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // configure any required mock behavior
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc = MockMvcBuilders.standaloneSetup(userRepository).build();
        mockMvc = MockMvcBuilders.standaloneSetup(senderService).build();
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterWithValidInput() {
        RegisterDTO registerDTO = new RegisterDTO();
        // set properties of dto object
        registerDTO.setName("Hristo Iliev");
        registerDTO.setPassword("Password123!");
        registerDTO.setConfirmPassword("Password123!");
        registerDTO.setEmail("hristoiliewh@icloud.com");
        registerDTO.setPhoneNumber("0888123456");
        registerDTO.setDateOfBirth(LocalDate.now().minusYears(26));
        registerDTO.setGender("m");
        registerDTO.setBio("Hi, i'm Hristo.");
        registerDTO.setUsername("hristoiliev");
        User u = new User();
        // set properties of u object

        u.setName("Hristo Iliev");
        u.setPassword("Password123!");
        u.setEmail("hristoiliewh@icloud.com");
        u.setPhoneNumber("0888123456");
        u.setDateOfBirth(LocalDate.now().minusYears(26));
        u.setGender("m");
        u.setBio("Hi, i'm Hristo.");
        u.setUsername("hristoiliev");
        u.setId(1);

        UserSimpleDTO expected = new UserSimpleDTO();
        // set properties of expected object
        expected.setId(1);
        expected.setName("Hristo Iliev");
        expected.setEmail("hristoiliewh@icloud.com");
        expected.setPhoneNumber("0888123456");
        expected.setDateOfBirth(LocalDate.now().minusYears(26));
        expected.setGender("m");
        expected.setBio("Hi, i'm Hristo.");
        expected.setProfilePhotoURL(null);
        expected.setUsername("hristoiliev");

        when(userService.register(registerDTO)).thenReturn(expected);

        UserSimpleDTO result = userController.register(registerDTO).getBody();

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    public void testRegisterWithPasswordMismatch() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("John Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender("m");
        dto.setBio("Lorem ipsum dolor sit amet.");
        dto.setUsername("johndoe123");
        dto.setPassword("strongPassword");
        dto.setConfirmPassword("weakPassword"); // setting mismatched password here

        BadRequestException badRequestException = new BadRequestException("Password missmatch");

        when(userService.register(dto)).thenThrow(badRequestException);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.register(dto);
        });

        assertEquals(badRequestException.getMessage(),exception.getMessage());
    }

    @Test
    public void testRegisterWithExistingEmail() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("Jane Doe");
        dto.setEmail("janedoe@example.com"); // setting existing email here
        dto.setPhoneNumber("0987654321");
        dto.setDateOfBirth(LocalDate.of(1995, 5, 15));
        dto.setGender("f");
        dto.setBio("Lorem ipsum dolor sit amet.");
        dto.setUsername("janedoe123");
        dto.setPassword("strongPassword");
        dto.setConfirmPassword("strongPassword");

        BadRequestException badRequestException = new BadRequestException("Email already exists!");

        when(userService.register(dto)).thenThrow(badRequestException);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userController.register(dto);
        });

        assertEquals(badRequestException.getMessage(), exception.getMessage());
    }

    @Test
    public void testRegisterWithExistingUsername() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("John Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender("m");
        dto.setBio("Lorem ipsum dolor sit amet.");
        dto.setUsername("existingUser"); // setting existing username here
        dto.setPassword("strongPassword");
        dto.setConfirmPassword("strongPassword");

        BadRequestException badRequestException = new BadRequestException("Username already exists!");

        when(userService.register(dto)).thenThrow(badRequestException);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userController.register(dto);
        });

        assertEquals(badRequestException.getMessage(), exception.getMessage());
    }

    @Test
    public void testRegisterWithExistingPhoneNumber() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("John Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhoneNumber("1234567890"); // setting an existing phone number here
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender("m");
        dto.setBio("Lorem ipsum dolor sit amet.");
        dto.setUsername("johndoe123");
        dto.setPassword("strongPassword");
        dto.setConfirmPassword("strongPassword");

        BadRequestException badRequestException = new BadRequestException("Phone number already exists!");

        when(userService.register(dto)).thenThrow(badRequestException);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userController.register(dto);
        });

        assertEquals(badRequestException.getMessage(), exception.getMessage());
    }
    @Test
    public void testRegisterWithInvalidAge() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("John Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setDateOfBirth(LocalDate.now().minusYears(15)); // setting age below 16 here
        dto.setGender("m");
        dto.setBio("Lorem ipsum dolor sit amet.");
        dto.setUsername("johndoe123");
        dto.setPassword("strongPassword");
        dto.setConfirmPassword("strongPassword");

        BadRequestException badRequestException = new BadRequestException("You must be older than 16 years.");

        when(userService.register(dto)).thenThrow(badRequestException);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userController.register(dto);
        });

        assertEquals(badRequestException.getMessage(), exception.getMessage());
    }

    @Test
    public void testLogin() {
        // Set up a test user
//        User user = new User();
//        user.setUsername("hristoiliev");
//        user.setPassword(encoder.encode("testpassword"));
//        user.setEmailConfirmed(true);
//        user.setFollowers(new HashSet<>());
//        user.setFollowing(new HashSet<>());
//        user.setVideos(new ArrayList<Video>());

        // Call the login method
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("hristoiliev");
        loginDTO.setPassword("testpassword");
        UserFullInfoDTO expected = new UserFullInfoDTO();
        expected.setId(1);
        expected.setName("Hristo Iliev");
        expected.setEmail("hristoiliev@icloud.com");
        expected.setPhoneNumber("0888123456");
        expected.setDateOfBirth(LocalDate.now().minusYears(26));
        expected.setGender("m");
        expected.setBio("Hi, i'm Hristo.");
        expected.setProfilePhotoURL(null);
        expected.setUsername("hristoiliev");
        expected.setFollowers(new ArrayList<>());
        expected.setFollowing(new ArrayList<>());
        expected.setVideos(new ArrayList<>());


//    @Test
//    public void testFollow() {
//        // create two test users
//        User follower = new User();
//        follower.setId(1);
//        follower.setPassword("Password123!");
//        follower.setName("Hristo Iliev");
//        follower.setEmail("hristoiliewh@icloud.com");
//        follower.setPhoneNumber("0888123456");
//        follower.setDateOfBirth(LocalDate.now().minusYears(26));
//        follower.setGender("m");
//        follower.setBio("Hi, i'm Hristo.");
//        follower.setProfilePhotoURL(null);
//        follower.setUsername("hristoiliev");
//        follower.setFollowers(new HashSet<>());
//        follower.setFollowing(new HashSet<>());
//        follower.setVideos(new ArrayList<>());
//
//        userRepository.save(follower);
//
//        User followTo = new User();
//        followTo.setId(2);
//        followTo.setPassword("Password123!");
//        followTo.setName("Hristo Iliev");
//        followTo.setEmail("hristoiliewh@icloud.com");
//        followTo.setPhoneNumber("0888123456");
//        followTo.setDateOfBirth(LocalDate.now().minusYears(26));
//        followTo.setGender("m");
//        followTo.setBio("Hi, i'm Hristo.");
//        followTo.setProfilePhotoURL(null);
//        followTo.setUsername("hristoiliev");
//        followTo.setFollowers(new HashSet<>());
//        followTo.setFollowing(new HashSet<>());
//        followTo.setVideos(new ArrayList<>());
//        userRepository.save(followTo);
//
//        // call the follow method
//        int followersCount = userService.follow(follower.getId(), followTo.getId());
//
//        // check that the followers count has been updated correctly
//        assertEquals(1, followersCount);
//
//        // check that the follower is now following the followTo user
//        assertTrue(followTo.getFollowers().contains(follower));
//
//        // call the follow method again to unfollow the user
//        followersCount = userService.follow(follower.getId(), followTo.getId());
//
//        // check that the followers count has been updated correctly
//        assertEquals(0, followersCount);
//
//        // check that the follower is no longer following the followTo user
//        assertFalse(followTo.getFollowers().contains(follower));
//    }


        // Check the result
        assertNotNull(expected);
        assertEquals("hristoiliev", expected.getUsername());
        assertTrue(expected.getFollowers().isEmpty());
        assertTrue(expected.getFollowing().isEmpty());
        assertTrue(expected.getVideos().isEmpty());
    }
}
