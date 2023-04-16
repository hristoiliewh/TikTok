package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>{

    @Query(value = "SELECT * FROM videos WHERE caption LIKE %:name%", nativeQuery = true)
    List<Video> findAllContains(@Param("name") String name);

}
