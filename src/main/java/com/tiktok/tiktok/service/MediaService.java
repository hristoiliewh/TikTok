package com.tiktok.tiktok.service;

<<<<<<< HEAD
import com.tiktok.tiktok.model.DTOs.UserWithoutPassDTO;
import com.tiktok.tiktok.model.entities.Sound;
=======
import com.tiktok.tiktok.model.DTOs.UserSimpleDTO;
>>>>>>> cdc216b247d57d29cde4b2f0d5f51c76de114135
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.repositories.SoundRepository;
import com.tiktok.tiktok.model.repositories.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            User u = getUserById(userId);

            String path = uploadMedia(origin);
            u.setProfilePhotoURL(path);
            userRepository.save(u);
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
            return mapper.map(u, UserSimpleDTO.class);
        } catch (IOException e) {
            System.out.println("IOException by copying photo.");
            throw new BadRequestException("Failed uploading photo.");
        }
    }

    public static final int MAX_VIDEO_CAPTION_LENGTH = 200;


    public Video uploadVideo(int id, MultipartFile origin, String caption, Boolean isPrivate, int soundId) {
        try {
            User user = getUserById(id);

            validateVideoInfo(origin, caption);

            Video video = new Video();
            video.setCaption(caption);
            video.setCreatedAt(LocalDateTime.now());
            video.setOwner(user);
            video.setPrivate(isPrivate);
            Optional<Sound> sound = soundRepository.findById(soundId);
            video.setSound(sound.isPresent() ? sound.get() : null);
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
            String path = uploadMedia(file);
            video.setUrl(path);

            videoRepository.save(video);
            return video;
        } catch (IOException e) {
            System.out.println("IOException by copying a media file.");
            throw new BadRequestException("Failed uploading media.");
        }
    }



    private void validateVideoInfo(MultipartFile file, String caption) {
        if (file.isEmpty()) {
            throw new BadRequestException("The file is not attached!");
        }
        if (caption.length() > MAX_VIDEO_CAPTION_LENGTH) {
            throw new BadRequestException("The video caption is too long. Please enter caption up to 200 symbols.");
        }
        if (!file.getContentType().equals("video/mp4")) {
            throw new BadRequestException("Invalid video format!");
        }
    }
}
