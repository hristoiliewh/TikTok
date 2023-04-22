package com.tiktok.tiktok.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class VideosReactionsKey implements Serializable {
    @Column(name = "video_id")
    private int videoId;

    @Column(name = "user_id")
    private int userId;
}
