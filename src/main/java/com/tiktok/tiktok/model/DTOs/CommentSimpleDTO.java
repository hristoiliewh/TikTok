package com.tiktok.tiktok.model.DTOs;

import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CommentSimpleDTO extends CommentWithoutVideoDTO{
    private VideoWithoutOwnerDTO video;

}
