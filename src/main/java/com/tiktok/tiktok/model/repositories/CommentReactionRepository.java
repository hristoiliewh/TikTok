package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReactions, Integer> {
    Optional<CommentReactions> findByCommentAndUser(Comment comment, User user);
}
