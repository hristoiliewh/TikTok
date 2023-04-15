package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {

    Optional<Sound> findByName(String soundName);

    boolean existsByName(String name);

    @Query(value = "SELECT id, name, url FROM sounds WHERE name LIKE %?1%")
    List<Sound> findAllContains(String name);

}
