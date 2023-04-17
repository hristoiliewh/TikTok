package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.Hashtag;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.repositories.HashtagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HashtagService extends AbstractService {

    @Autowired
    private HashtagRepository hashtagRepository;

    public Hashtag upload(String hashtag) {
        if (!isValidHashtag(hashtag)) {
            throw new BadRequestException("Hashtags can contains only letters and/or digits and should be at least 3 digits long.");
        }
        Hashtag tag = new Hashtag();
        if (hashtagRepository.findByTag("#" + hashtag) == null) {
            tag.setTag("#" + hashtag);
            hashtagRepository.save(tag);
            return tag;
        } else {
            return hashtagRepository.findByTag("#" + hashtag);
        }
    }

    private boolean isValidHashtag(String hashtag) {
        String pattern = "^[a-zA-Z0-9._-]{3,30}$";
        return hashtag.matches(pattern);
    }

    public Set<Hashtag> checkForHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                hashtags.add(word.substring(1));
            }
        }
        Set<Hashtag> validHashtags = new HashSet<>();
        for (String h : hashtags) {
            validHashtags.add(upload(h));
        }
        return validHashtags;
    }
}
