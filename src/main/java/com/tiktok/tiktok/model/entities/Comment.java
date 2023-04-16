package com.tiktok.tiktok.model.entities;

import com.tiktok.tiktok.model.DTOs.CommentReactionDTO;
import com.tiktok.tiktok.model.DTOs.CommentWithoutVideoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String text;
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> repliedComments;
    @JoinColumn()
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "comment")
    Set<CommentReactions> reactions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}