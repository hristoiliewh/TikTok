package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoWithoutOwnerDTO {
    private int id;

    private String caption;

    private LocalDateTime createdAt;

    private boolean isPrivate;

    private String url;

    private SoundSimpleDTO sound;

    private List<Comment> comments;
}
