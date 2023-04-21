package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

    @Query(value = "SELECT * FROM videos AS v " +
            "WHERE caption LIKE %:name% " +
            "AND (v.is_private = FALSE " +
            "OR (v.is_private = TRUE AND v.owner_id = :loggedUserId)) " +
            "ORDER BY v.views DESC", nativeQuery = true)
    Page<Video> findAllContains(@Param("name") String name,int loggedUserId, Pageable pageable);

    Optional<Video> findByUrl(String url);
    Page<Video> findAllByOwnerId(int userId, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM videos AS v " +
            "WHERE v.is_private = FALSE " +
            "OR (v.is_private = TRUE AND v.owner_id = :loggedUserId) " +
            "ORDER BY v.views DESC", nativeQuery = true)
    Page<Video> showAllVideosByViews(int loggedUserId, Pageable pageable);

    @Query(value = "SELECT v.id, v.caption, v.owner_id, v.created_at, v.is_private, v.sound_id, v.url, v.views " +
            "FROM videos v " +
            "JOIN video_tags t ON v.id = t.video_id " +
            "JOIN hashtags h ON h.id = t.hashtag_id " +
            "JOIN users u ON u.id = v.owner_id " +
            "WHERE h.tag = :hashtag " +
            "AND (v.is_private = FALSE " +
            "OR (v.is_private = TRUE AND v.owner_id = :loggedUserId)) " +
            "ORDER BY v.views DESC", nativeQuery = true)
    Page<Video> findAllNotPrivateVideosByHashtag(@Param("hashtag") String hashtag, @Param("loggedUserId") int loggedUserId, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM videos AS v " +
            "WHERE v.is_private = FALSE " +
            "AND v.owner_id = :userId " +
            "ORDER BY v.created_at DESC", nativeQuery = true)
    Page<Video> showAllVideosCreatedAt(int userId, Pageable pageable);
}
