package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdOwnerParentDTO {
    private int id;
    private UserWithPicNameIdDTO owner;
    private String comment;
    private CommentWithOwnerAndIDDTO parentComment;
}
