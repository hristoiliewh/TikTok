package com.tiktok.tiktok.model.DTOs.videosDTOs;
import com.tiktok.tiktok.model.DTOs.usersDTOs.UserWithPicNameIdDTO;
import com.tiktok.tiktok.model.DTOs.soundsDTOs.SoundDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VideoSimpleDTO {
    private int id;
    private String url;
    private LocalDateTime createdAt;
    private boolean isPrivate;
    private String caption;
    private int numberOfReactions;
    private int views;
    private int numberOfComments;
    private UserWithPicNameIdDTO owner;
    private SoundDTO sound;
}
