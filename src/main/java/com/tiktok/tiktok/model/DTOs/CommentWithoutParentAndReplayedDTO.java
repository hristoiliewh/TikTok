package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithoutParentAndReplayedDTO extends CommentWithOwnerAndIDDTO{
    private VideoWithoutOwnerDTO video;
}
