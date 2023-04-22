package com.tiktok.tiktok;

import com.tiktok.tiktok.controller.UserController;
import com.tiktok.tiktok.controller.VideoController;
import com.tiktok.tiktok.model.DTOs.usersDTOs.UserWithPicNameIdDTO;
import com.tiktok.tiktok.model.DTOs.videosDTOs.VideoDeletedDTO;
import com.tiktok.tiktok.model.DTOs.videosDTOs.VideoReactionDTO;
import com.tiktok.tiktok.model.DTOs.videosDTOs.VideoSimpleDTO;
import com.tiktok.tiktok.model.entities.Hashtag;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.entities.VideoReactions;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.repositories.HashtagRepository;
import com.tiktok.tiktok.model.repositories.UserRepository;
import com.tiktok.tiktok.model.repositories.VideoRepository;
import com.tiktok.tiktok.service.MailSenderService;
import com.tiktok.tiktok.service.UserService;
import com.tiktok.tiktok.service.VideoService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.Assert.assertEquals;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikTokApplicationTests.class)
public class VideoServiceTest {
    @InjectMocks
    private VideoService videoService;
    @InjectMocks
    private UserService userService;

    @InjectMocks
    private VideoController videoController;

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private HashtagRepository hashtagRepository;
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
        mockMvc = MockMvcBuilders.standaloneSetup(videoRepository).build();
        mockMvc = MockMvcBuilders.standaloneSetup(hashtagRepository).build();
//        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = NotFoundException.class)
    public void testGetMyLikedVideosNoVideosFound() {
        // Arrange
        int loggedUserId = 1;
        int page = 0;
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<Video> videoPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(videoRepository.findAllByReactions(loggedUserId, pageable)).thenReturn(videoPage);

        // Act
        videoService.getMyLikedVideos(loggedUserId, page, limit);
    }
    @org.junit.Test
    public void testGetByHashtagSuccess() {
        // Arrange
        String hashtag = "test";
        int loggedUserId = 1;
        int page = 0;
        int limit = 10;
        Hashtag tag = new Hashtag();
        tag.setId(1);
        tag.setTag("#" + hashtag);
        when(hashtagRepository.existsByTag("#" + hashtag)).thenReturn(true);

        User user = new User();
        user.setId(1);

        Video video = new Video();
        video.setId(1);
        video.setOwner(user);
        video.setCreatedAt(LocalDateTime.now());
        video.setPrivate(false);
        video.setUrl("URL");
        video.setViews(1);

        UserWithPicNameIdDTO userWithPicNameIdDTO = new UserWithPicNameIdDTO();
        userWithPicNameIdDTO.setId(1);
        when(videoRepository.findAllNotPrivateVideosByHashtag("#" + hashtag, loggedUserId, PageRequest.of(page, limit)))
                .thenReturn(new PageImpl<>(Collections.singletonList(video)));

        VideoSimpleDTO videoSimpleDTO = new VideoSimpleDTO();
        videoSimpleDTO.setId(video.getId());
        videoSimpleDTO.setOwner(userWithPicNameIdDTO);
        videoSimpleDTO.setPrivate(video.isPrivate());
        videoSimpleDTO.setUrl(video.getUrl());
        videoSimpleDTO.setViews(video.getViews());
        videoSimpleDTO.setCreatedAt(video.getCreatedAt());
        when(mapper.map(video, VideoSimpleDTO.class)).thenReturn(videoSimpleDTO);

        // Act
        List<VideoSimpleDTO> result = videoService.getByHashtag(hashtag, loggedUserId, page, limit);

        // Assert
        assertEquals (1, result.size());
        assertEquals (videoSimpleDTO.getId(), result.get(0).getId());
        assertEquals(videoSimpleDTO.getOwner(), result.get(0).getOwner());
        assertEquals(videoSimpleDTO.isPrivate(), result.get(0).isPrivate());
        assertEquals(videoSimpleDTO.isPrivate(), result.get(0).isPrivate());
        assertEquals (videoSimpleDTO.getUrl(), result.get(0).getUrl());
        assertEquals (videoSimpleDTO.getViews(), result.get(0).getViews());
        assertEquals (videoSimpleDTO.getCreatedAt(), result.get(0).getCreatedAt());
        verify(hashtagRepository, times(1)).existsByTag("#" + hashtag);
        verify(videoRepository, times(1)).findAllNotPrivateVideosByHashtag("#" + hashtag, loggedUserId, PageRequest.of(page, limit));
        verify(mapper, times(1)).map(video, VideoSimpleDTO.class);
    }


}
