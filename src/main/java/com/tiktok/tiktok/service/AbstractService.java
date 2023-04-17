package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected VideoRepository videoRepository;
    @Autowired
    protected SoundRepository soundRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected ModelMapper mapper;

    protected User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    protected Video getVideoById(int id) {
        return videoRepository.findById(id).orElseThrow(() -> new NotFoundException("Video not found"));
    }

    protected Comment getCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    protected Sound getSoundById(int soundId) {
        return soundRepository.findById(soundId).orElseThrow(() -> new NotFoundException("Sound not found"));
    }

    protected boolean isPossibleToWatch(Video video, int userId) {
        if (video.isPrivate() && video.getOwner().getId() != userId) {
            return false;
        }
        return true;
    }
}
