package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoSimpleDTO {

    private int id;

    private String caption;

    private LocalDateTime createdAt;

    private boolean isPrivate;

    private String url;

    private UserSimpleDTO owner;

    private SoundSimpleDTO sound;

}
