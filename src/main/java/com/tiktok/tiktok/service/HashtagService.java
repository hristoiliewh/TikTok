package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.Hashtag;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.repositories.HashtagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashtagService extends AbstractService{

    @Autowired
    private ModelMapper mapper;

    public void upload(String hashtag) {
        if (!isValidHashtag(hashtag)){
            throw new BadRequestException("Hashtags can contains only letters and/or digits and should be at least 3 digits long.");
        }
        Hashtag tag = new Hashtag();
        tag.setTag("#" + hashtag);

        hashtagRepository.save(tag);
    }

    private boolean isValidHashtag(String hashtag){
        String pattern = "^[a-zA-Z0-9._-]{3,30}$";
        return hashtag.matches(pattern);
    }

    public void checkForHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                hashtags.add(word.substring(1));
            }
        }
        for (String h : hashtags){
            upload(h);
        }
    }
}
