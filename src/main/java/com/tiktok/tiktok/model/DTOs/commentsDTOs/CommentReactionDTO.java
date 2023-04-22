package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import com.tiktok.tiktok.model.DTOs.usersDTOs.UserReactionsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentReactionDTO {

    private CommentWithIdAndOwnerDTO comment;
    private UserReactionsDTO user;
    private boolean isLiked;
}
