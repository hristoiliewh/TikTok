package com.tiktok.tiktok.model.DTOs.commentsDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class CommentDeletedDTO {
    @Setter
    private int id;
    private String deleteText = "Comment deleted successfully";
}
