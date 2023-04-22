package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdOwnerParentDTO extends CommentWithIdAndOwnerDTO {
    private CommentWithIdAndOwnerDTO parentComment;
}
