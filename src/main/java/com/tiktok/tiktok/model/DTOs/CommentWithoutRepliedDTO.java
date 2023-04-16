package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentWithoutRepliedDTO {
    private int id;
    private String comment;
    private UserSimpleDTO owner;
    private CommentWithoutVideoDTO parentComment;
    private LocalDateTime createdAt;
    private VideoWithoutOwnerDTO video;

}
