package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.videosDTOs.*;
import com.tiktok.tiktok.model.entities.*;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.repositories.HashtagRepository;
import com.tiktok.tiktok.model.repositories.VideoReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VideoService extends AbstractService {
    @Autowired
    private VideoReactionRepository videoReactionRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public VideoDeletedDTO deleteVideo(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (video.getOwner().getId() != loggedUserId) {
            throw new BadRequestException("You don't have permission to delete this video");
        }
        videoRepository.deleteById(videoId);
        logger.info("Video deleted successfully: " + videoId);
        return mapper.map(video, VideoDeletedDTO.class);
    }

    public VideoSimpleDTO getById(int videoId, int loggedUserId) {
        Optional<Video> video = videoRepository.findById(videoId, loggedUserId);
        if (video.isEmpty()){
            throw new BadRequestException("Video not found.");
        }
        int reactions = video.get().getReactions().size();
        int comments = video.get().getComments().size();
        VideoSimpleDTO videoSimpleDTO = mapper.map(video, VideoSimpleDTO.class);
        videoSimpleDTO.setNumberOfReactions(reactions);
        videoSimpleDTO.setNumberOfComments(comments);
        return videoSimpleDTO;
    }

    public Page<VideoSimpleDTO> getAllVideos(int userId, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<Video> videoPage = videoRepository.findAllByOwnerId(userId, loggedUserId,pageable);
        return mapVideosToDTOs(videoPage);
    }

    public Page<VideoSimpleDTO> getByName(String videoName, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Video> videos = videoRepository.findAllContains(videoName, loggedUserId, pageable);
        return mapVideosToDTOs(videos);
    }

    public Page<VideoSimpleDTO> getByHashtag(String hashtag, int loggedUserId, int page, int limit) {
        if (!hashtagRepository.existsByTag("#" + hashtag)) {
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        pageable = PageRequest.of(page, limit);
        Page<Video> videos = videoRepository.findAllNotPrivateVideosByHashtag("#" + hashtag,loggedUserId, pageable);
        return mapVideosToDTOs(videos);
    }

    public VideoReactionDTO likeDislike(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        isPossibleToWatch(video, loggedUserId);
        User user = getUserById(loggedUserId);
        Optional<VideoReactions> videoReactions = videoReactionRepository.findByVideoAndUser(video, user);
        if (videoReactions.isPresent()) {
            VideoReactions reactions1 = videoReactions.get();
            reactions1.setLiked(!reactions1.isLiked());
            videoReactionRepository.delete(reactions1);
            return mapper.map(reactions1, VideoReactionDTO.class);
        } else {
            VideoReactions reactions = new VideoReactions();
            reactions.setUser(user);
            reactions.setVideo(video);
            reactions.setLiked(true);
            videoReactionRepository.save(reactions);
            return mapper.map(reactions, VideoReactionDTO.class);
        }
    }

    public int getReactions(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        isPossibleToWatch(video, loggedUserId);
        return video.getReactions().size();
    }

    public Page<VideoSimpleDTO> showFeed(int loggedUserId, int pageNumber, int videosPerPage) {
        pageable = PageRequest.of(pageNumber, videosPerPage, Sort.by("created_at").descending());
        Page<Video> videos = videoRepository.showAllVideosByViews(loggedUserId, pageable);
        return mapVideosToDTOs(videos);
    }

    public Page<VideoSimpleDTO> getMyLikedVideos(int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<Video> videos =  videoRepository.findAllByReactions(loggedUserId, pageable);
        return mapVideosToDTOs(videos);
    }

    private Page<VideoSimpleDTO> mapVideosToDTOs(Page<Video> videos){
        if (videos.getContent().size() == 0) {
            throw new NotFoundException("No videos found.");
        }
        Page<VideoSimpleDTO> videoPage = videos.map(v -> mapper.map(v, VideoSimpleDTO.class));
        for (int i = 0; i < videoPage.getContent().size(); i++) {
            int reactions = videos.getContent().get(i).getReactions().size();
            int comments = videos.getContent().get(i).getComments().size();
            videoPage.getContent().get(i).setNumberOfReactions(reactions);
            videoPage.getContent().get(i).setNumberOfComments(comments);
        }
        return videoPage;
    }
}
