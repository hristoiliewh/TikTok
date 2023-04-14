package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

}
