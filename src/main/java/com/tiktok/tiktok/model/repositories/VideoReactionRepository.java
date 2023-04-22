package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.entities.VideoReactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReactions, Integer> {
    Optional<VideoReactions> findByVideoAndUser(Video video, User user);
}
