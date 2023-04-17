package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CommentWithoutVideoAndParentComment {
    private int id;
    private String comment;
    private UserSimpleDTO owner;
    private List<CommentWithoutVideoAndParentComment> repliedComments;
    private LocalDateTime createdAt;
}

