package com.tiktok.tiktok.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String caption;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "is_private")
    private boolean isPrivate;
    @Column()
    private String url;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "sound_id", referencedColumnName = "id")
    private Sound sound;

    @OneToMany(mappedBy = "video")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "video_tags",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();

    @OneToMany(mappedBy = "video")
    private Set<VideoReactions> reactions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Video video)) return false;
        return id == video.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
