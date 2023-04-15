package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
public class CommentSimpleDTO {
    private int id;
    private String text;
    private UserSimpleDTO owner;
    private VideoWithoutOwnerDTO video;
    //    private Comment parentComment;
    private LocalDateTime createdAt;
}
