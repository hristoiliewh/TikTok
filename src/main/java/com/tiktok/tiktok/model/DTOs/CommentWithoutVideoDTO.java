package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
public class CommentWithoutVideoDTO {
    private int id;
    private String text;
    private UserSimpleDTO owner;
    //    private Comment parentComment;
    private LocalDateTime createdAt;
}
