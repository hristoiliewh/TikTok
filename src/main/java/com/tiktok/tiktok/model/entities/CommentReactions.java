package com.tiktok.tiktok.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users_react_to_comments")
@Getter
@Setter
@NoArgsConstructor
public class CommentReactions {
    @EmbeddedId
    CommentsReactionsKey id = new CommentsReactionsKey();

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_liked")
    private boolean isLiked;
}

