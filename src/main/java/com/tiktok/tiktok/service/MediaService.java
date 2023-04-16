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
    public UserSimpleDTO upload(MultipartFile origin, int userId) {
        String contentType = origin.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new BadRequestException("Only JPEG and PNG images are allowed");
        }
        User u = getUserById(userId);

        String path = uploadMedia(origin);
        u.setProfilePhotoURL(path);
        userRepository.save(u);

        return mapper.map(u, UserSimpleDTO.class);
    }

    private String uploadMedia(MultipartFile origin) {
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
            System.out.println("IOException by copying media.");
            throw new BadRequestException("Failed uploading media.");
        }
    }

    public VideoSimpleDTO uploadVideo(int id, MultipartFile origin, String caption, Boolean isPrivate, int soundId) {
        User user = getUserById(id);
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
    }


    private void validateVideoInfo(MultipartFile origin, String caption) {
        if (origin.isEmpty()) {
            throw new BadRequestException("The file is not attached!");
        }
        if (caption.length() > 200) {
            throw new BadRequestException("The video caption is too long. Please enter caption up to 200 symbols.");
        }
        if (!origin.getContentType().equals("video/mp4")) {
            throw new BadRequestException("Invalid video format!");
        }
    }

    public SoundSimpleDTO uploadSound(MultipartFile origin, String name) {
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

    public File download(String fileName) {
        fileName = "uploads" + File.separator + fileName;
        File f = new File(fileName);
        if(f.exists()){
            return f;
        }
        throw new NotFoundException("File not found");
    }
}
