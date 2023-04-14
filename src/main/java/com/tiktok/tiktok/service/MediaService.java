package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service

public class MediaService extends AbstractService{
    public UserSimpleDTO upload(MultipartFile origin, int userId) {
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
    
    public Video uploadVideo(int id, MultipartFile origin, String caption, Boolean isPrivate, int soundId) {
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

        videoRepository.save(video);
        return video;
    }


    private void validateVideoInfo(MultipartFile file, String caption) {
        if (file.isEmpty()) {
            throw new BadRequestException("The file is not attached!");
        }
        if (caption.length() > 200) {
            throw new BadRequestException("The video caption is too long. Please enter caption up to 200 symbols.");
        }
        if (!file.getContentType().equals("video/mp4")) {
            throw new BadRequestException("Invalid video format!");
        }
    }
}
