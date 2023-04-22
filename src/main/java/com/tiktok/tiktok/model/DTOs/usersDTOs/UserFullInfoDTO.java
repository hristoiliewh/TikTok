package com.tiktok.tiktok.model.DTOs.usersDTOs;

import com.tiktok.tiktok.model.DTOs.videosDTOs.VideoWithoutOwnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserFullInfoDTO extends UserSimpleDTO {
    private List<UserSimpleDTO> followers;
    private List<UserSimpleDTO> following;
    private List<VideoWithoutOwnerDTO> videos;
}
