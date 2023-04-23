package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CommentWithIdOwnerRepliedDTO extends CommentWithIdAndOwnerDTO {
    private List<CommentWithIdOwnerRepliedDTO> repliedComments;
}

