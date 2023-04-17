package com.tiktok.tiktok.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserFullInfoDTO extends UserSimpleDTO{
    private List<UserSimpleDTO> followers;
    private List<UserSimpleDTO> following;
    private List<VideoWithoutOwnerDTO> videos;
}
