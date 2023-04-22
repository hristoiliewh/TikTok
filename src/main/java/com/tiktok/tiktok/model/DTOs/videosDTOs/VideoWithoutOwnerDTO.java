package com.tiktok.tiktok.model.DTOs.videosDTOs;

import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VideoWithoutOwnerDTO {
    private int id;
    private String caption;
    private LocalDateTime createdAt;
    private boolean isPrivate;
    private String url;
    private SoundDTO sound;
}
