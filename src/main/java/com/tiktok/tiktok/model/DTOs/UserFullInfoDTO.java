package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserFullInfoDTO extends UserSimpleDTO{

    private String profilePhotoURL;
    private List<UserSimpleDTO> followers;
    private List<UserSimpleDTO> following;
    private List<VideoWithoutOwnerDTO> videos;
}
