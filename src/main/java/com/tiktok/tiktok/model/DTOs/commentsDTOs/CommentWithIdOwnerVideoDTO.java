package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import com.tiktok.tiktok.model.DTOs.videosDTOs.VideoWithoutOwnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdOwnerVideoDTO extends CommentWithIdAndOwnerDTO {
    private VideoWithoutOwnerDTO video;
}
