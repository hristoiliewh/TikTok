package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithOwnerAndIDDTO {
    private int id;
    private String comment;
    private LocalDateTime createdAt;
    private UserWithPicNameIdDTO owner;

}
