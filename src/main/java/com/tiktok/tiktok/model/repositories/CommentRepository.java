package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
//    @Query(value = "SELECT * FROM comments WHERE video_id = :videoId ORDER BY created_at DESC", nativeQuery = true)
//    Page<Comment> findAllByVideoIdAndCreatedAt(Pageable pageable, @Param("videoId") int videoId);

    @Query(value = "SELECT comments.id, comments.comment, comments.video_id, comments.owner_id, comments.parent_id, comments.created_at FROM comments " +
            "JOIN videos ON videos.id = comments.video_id " +
            "WHERE comments.id = :commentId " +
            "AND (videos.is_private = FALSE " +
            "OR (videos.is_private = TRUE AND videos.owner_id = :loggedUserId))", nativeQuery = true)
    Optional<Comment> findById(int commentId, int loggedUserId);

    @Query(value = "SELECT comments.id, comments.comment, comments.video_id, comments.owner_id, comments.parent_id, comments.created_at FROM comments " +
            "JOIN videos ON videos.id = comments.video_id " +
            "WHERE videos.id = :videoId " +
            "AND (videos.is_private = FALSE " +
            "OR (videos.is_private = TRUE AND videos.owner_id = :loggedUserId))", nativeQuery = true)
    Page<Comment> findAllByVideoId(int videoId, int loggedUserId, Pageable pageable);

    @Query(value = "SELECT comments.id, comments.comment, comments.video_id, comments.owner_id, comments.parent_id, comments.created_at FROM comments " +
            "JOIN videos ON videos.id = comments.video_id " +
            "WHERE videos.id = :videoId " +
            "AND (videos.is_private = FALSE " +
            "OR (videos.is_private = TRUE AND videos.owner_id = :loggedUserId))", nativeQuery = true)
    Page<Comment> findAllByVideoIdAndCreatedAt(int videoId, int loggedUserId, Pageable pageable);


}
