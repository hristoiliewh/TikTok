package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdOwnerParentDTO {
    private int id;
    private String comment;
    private UserWithPicNameIdDTO owner;
    private LocalDateTime createdAt;
    private CommentWithOwnerAndIDDTO parentComment;
}
