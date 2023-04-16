package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentReactionDTO {

    private CommentWithoutRepliedDTO comment;
    private UserReactionsDTO user;
    private boolean isLiked;
}