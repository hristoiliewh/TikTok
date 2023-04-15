package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {


    boolean existsByName(String name);

    @Query(value = "SELECT * FROM sounds WHERE name LIKE %:name%", nativeQuery = true)
    List<Sound> findAllContains(@Param("name") String name);

}
