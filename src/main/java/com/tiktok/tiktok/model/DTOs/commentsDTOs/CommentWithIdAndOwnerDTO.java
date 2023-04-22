package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import com.tiktok.tiktok.model.DTOs.usersDTOs.UserWithPicNameIdDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithIdAndOwnerDTO {
    private int id;
    private String comment;
    private LocalDateTime createdAt;
    private UserWithPicNameIdDTO owner;

}
