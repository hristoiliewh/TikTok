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
public class CommentsReactionsKey implements Serializable {

    @Column(name = "comment_id")
    int commentId;

    @Column(name = "user_id")
    int userId;
}
