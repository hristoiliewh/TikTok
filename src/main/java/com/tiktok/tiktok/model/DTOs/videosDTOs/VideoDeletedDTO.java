package com.tiktok.tiktok.model.DTOs.videosDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class VideoDeletedDTO {
    @Setter
    private int id;
    private String deleteText = "Video deleted successfully";
}
