package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService extends AbstractService{

    public DeleteVideoDTO deleteVideo(int videoId, int loggedUserId) {
        Optional<Video> video = videoRepository.findById(videoId);
        if (!video.isPresent()){
            throw new NotFoundException("Video not found");
        }
        if (video.get().getOwner().getId() != loggedUserId){
            throw new UnauthorizedException("Can't delete this video");
        }
        videoRepository.deleteById(videoId);
        return new DeleteVideoDTO();
    }
}
