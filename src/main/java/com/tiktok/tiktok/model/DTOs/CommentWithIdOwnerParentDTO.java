package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdOwnerParentDTO {
    private int id;
    private String comment;
    private UserWithPicNameIdDTO owner;
    private CommentWithOwnerAndIDDTO parentComment;
}
