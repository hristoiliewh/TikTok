package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.SoundSimpleDTO;
import com.tiktok.tiktok.model.DTOs.VideoSimpleDTO;
import com.tiktok.tiktok.model.entities.Hashtag;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@Service

public class MediaService extends AbstractService {

    @Autowired
    private HashtagService hashtagService;

    public UserSimpleDTO uploadProfilePhoto(MultipartFile origin, int userId) throws Exception {
        try {
            String contentType = origin.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new BadRequestException("Only JPEG and PNG images are allowed");
            }
            User u = getUserById(userId);
            String path = uploadMedia(origin);
            u.setProfilePhotoURL(path);
            userRepository.save(u);
            return mapper.map(u, UserSimpleDTO.class);
        } catch (Exception e) {
            logger.error("Error while uploading profile photo for user id: " + userId, e);
            throw e;
        }
    }

    public VideoSimpleDTO uploadVideo(int userId, MultipartFile origin, String caption, Boolean isPrivate, int soundId) throws Exception {
        try {
            User user = getUserById(userId);
            validateVideoInfo(origin, caption);
            Video video = new Video();
            video.setCaption(caption);
            video.setCreatedAt(LocalDateTime.now());
            video.setOwner(user);
            video.setPrivate(isPrivate);
            Optional<Sound> sound = soundRepository.findById(soundId);
            video.setSound(sound.isPresent() ? sound.get() : null);
            String path = uploadMedia(origin);
            video.setUrl(path);
            Set<Hashtag> hashtags = hashtagService.checkForHashtags(caption);
            video.getHashtags().addAll(hashtags);
            videoRepository.save(video);
            return mapper.map(video, VideoSimpleDTO.class);
        } catch (Exception e) {
            logger.error("Error while uploading video for user id: " + userId, e);
            throw e;
        }
    }

    public SoundSimpleDTO uploadSound(MultipartFile origin, String name) throws Exception {
        try {
            String fileName = origin.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

            if (!fileExtension.equalsIgnoreCase("mp3") || fileExtension.equalsIgnoreCase("wav")) {
                throw new BadRequestException("Invalid sound format!");
            }
            if (soundRepository.existsByName(name)) {
                throw new BadRequestException("Name exists. Please enter another name of the sound.");
            }
            Sound sound = new Sound();
            String path = uploadMedia(origin);
            sound.setName(name);
            sound.setUrl(path);
            soundRepository.save(sound);
            return mapper.map(sound, SoundSimpleDTO.class);
        }
        catch (BadRequestException e){
            logger.error("Error occurred while uploading sound: {}", e.getMessage());
            throw e;
        }
    }

    private String uploadMedia(MultipartFile origin) throws Exception {
        try {
            String name = UUID.randomUUID().toString();
            String ext = FilenameUtils.getExtension(origin.getOriginalFilename());
            name = name + "." + ext;
            File dir = new File("uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir, name);
            Files.copy(origin.getInputStream(), f.toPath());
            String path = dir.getName() + File.separator + f.getName();
            return path;
        } catch (IOException e) {
            logger.error("Error occurred while uploading media: {}", e.getMessage());
            throw new Exception("Error uploading media. Please contact administration!");
        }
    }

    private void validateVideoInfo(MultipartFile origin, String caption) {
        if (origin.isEmpty()) {
            logger.error("The file is not attached!");
            throw new BadRequestException("The file is not attached!");
        }
        if (caption.length() > 200) {
            logger.error("The video caption is too long.");
            throw new BadRequestException("The video caption is too long. Please enter caption up to 200 symbols.");
        }
        if (!origin.getContentType().equals("video/mp4")) {
            logger.error("Invalid video format!");
            throw new BadRequestException("Invalid video format!");
        }
    }
}
