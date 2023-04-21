package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.*;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import com.tiktok.tiktok.model.repositories.HashtagRepository;
import com.tiktok.tiktok.model.repositories.VideoReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoService extends AbstractService {

    @Autowired
    private VideoReactionRepository videoReactionRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public VideoDeletedDTO deleteVideo(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (video.getOwner().getId() != loggedUserId) {
        }
        videoRepository.deleteById(videoId);
        logger.info("Video deleted successfully: " + videoId);
        return mapper.map(video, VideoDeletedDTO.class);
    }

    public VideoSimpleDTO getById(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        canWatch(video, loggedUserId);
        logger.info("Video found: " + videoId);
        return mapper.map(video, VideoSimpleDTO.class);
    }
    public List<VideoWithoutOwnerDTO> getAllVideos(int userId) {
        List<Video> videos = getUserById(userId).getVideos();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos found");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoWithoutOwnerDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> getByName(String videoName, int loggedUserId) {
        List<Video> videos = videoRepository.findAllContains(videoName);
        if (videos.size() == 0) {
            throw new NotFoundException("No videos with the given name found.");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos) {
            if (isPossibleToWatch(v, loggedUserId)) {
                videosNotPrivate.add(v);
            }
        }
        if (videosNotPrivate.size() == 0) {
            throw new NotFoundException("No videos with the given name found.");
        }
        return videosNotPrivate.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> getByHashtag(String hashtag, int loggedUserId) {
        if (!hashtagRepository.existsByTag("#" + hashtag)) {
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        Set<Video> videos = hashtagRepository.findByTag("#" + hashtag).getVideo();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos with the given hashtag found");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos) {
            if (isPossibleToWatch(v, loggedUserId)) {
                videosNotPrivate.add(v);
            }
        }
        if (videosNotPrivate.size() == 0) {
            throw new NotFoundException("No videos with the given hashtag found");
        }
        return videosNotPrivate.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public VideoReactionDTO likeDislike(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        canWatch(video, loggedUserId);
        User user = getUserById(loggedUserId);
        Optional<VideoReactions> videoReactions = videoReactionRepository.findByVideoAndUser(video, user);
        if (videoReactions.isPresent()) {
            VideoReactions reactions1 = videoReactions.get();
            reactions1.setLiked(!reactions1.isLiked());
            videoReactionRepository.delete(reactions1);
//            videoReactionRepository.save(reactions1);
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
        canWatch(video, loggedUserId);
        return video.getReactions().size();
    }
}
