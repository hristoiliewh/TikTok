package com.tiktok.tiktok.model.DTOs.videosDTOs;

import com.tiktok.tiktok.model.DTOs.usersDTOs.UserReactionsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoReactionDTO {
    private VideoWithoutOwnerDTO video;
    private UserReactionsDTO user;
    private boolean isLiked;
}
