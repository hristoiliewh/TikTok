package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.UserWithoutPassDTO;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class MediaService extends AbstractService{
    public UserWithoutPassDTO upload(MultipartFile origin, int userId) {

        try {
            User u = getUserById(userId);
            String name = UUID.randomUUID().toString();
            String ext = FilenameUtils.getExtension(origin.getOriginalFilename());

            name = name + "." + ext;
            File dir = new File("uploads");
            if(!dir.exists()){
                dir.mkdirs();
            }
            File f = new File(dir, name);
            Files.copy(origin.getInputStream(), f.toPath());

            String path = dir.getName() + File.separator + f.getName();
            u.setProfilePhotoURL(path);
            userRepository.save(u);
            return mapper.map(u, UserWithoutPassDTO.class);
        } catch (IOException e) {
            System.out.println("IOException by copying a photo.");
            throw new BadRequestException("Failed uploading photo.");
        }
    }
}
