package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Sound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM sounds WHERE name LIKE %:name%", nativeQuery = true)
    Page<Sound> findAllContains(@Param("name") String name, Pageable pageable);

}
