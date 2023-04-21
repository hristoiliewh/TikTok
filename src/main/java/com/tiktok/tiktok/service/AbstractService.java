package com.tiktok.tiktok.service;

import ch.qos.logback.classic.BasicConfigurator;
import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import com.tiktok.tiktok.model.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public abstract class AbstractService {
    protected static final Logger logger = LogManager.getLogger(AbstractService.class);

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
    protected static Pageable pageable;

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
    protected void canWatch(Video video, int loggedUserId){
        if (!isPossibleToWatch(video, loggedUserId)) {
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
    }
}
