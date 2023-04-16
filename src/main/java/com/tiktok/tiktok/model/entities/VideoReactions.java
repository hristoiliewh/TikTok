package com.tiktok.tiktok.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users_react_to_videos")
@Getter
@Setter
@NoArgsConstructor
public class VideoReactions {
    @EmbeddedId
    VideosReactionsKey id = new VideosReactionsKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("videoId")
    @JoinColumn(name = "video_id")
    Video video;

    @Column(name = "is_liked")
    boolean isLiked;
}
