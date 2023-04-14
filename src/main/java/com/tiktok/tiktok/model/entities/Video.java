package com.tiktok.tiktok.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "sound_id", referencedColumnName = "id")
    private Sound sound;
    @OneToMany(mappedBy = "video")
    private List<Comment> comments;

}
