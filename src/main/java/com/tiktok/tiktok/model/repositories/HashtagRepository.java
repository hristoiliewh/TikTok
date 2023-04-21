package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    boolean existsByTag(String tag);

    Hashtag findByTag(String tag);

}
