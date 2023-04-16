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
    private VideosReactionsKey id = new VideosReactionsKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("videoId")
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "is_liked")
    private boolean isLiked;
}
