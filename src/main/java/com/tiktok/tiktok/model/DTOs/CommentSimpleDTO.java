package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentSimpleDTO extends CommentWithoutVideoDTO {
    private VideoWithoutOwnerDTO video;
}
