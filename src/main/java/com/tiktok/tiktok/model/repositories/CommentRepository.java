package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comments WHERE video_id = :videoId ORDER BY created_at DESC", nativeQuery = true)
    Page<Comment> findAllByVideoIdAndCreatedAt(Pageable pageable, @Param("videoId") int videoId);
}
