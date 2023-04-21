package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VideoResponseDTO {

    private int id;
    private String url;
    private LocalDateTime createdAt;
    private boolean isPrivate;
    private String caption;
    private int numberOfReactions;
    private int views;
    private int numberOfComments;
    private UserWithPicNameIdDTO owner;
}
