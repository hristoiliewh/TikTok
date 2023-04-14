package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {

}
