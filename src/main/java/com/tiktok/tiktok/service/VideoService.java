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
        VideoSimpleDTO videoSimpleDTO = mapper.map(video, VideoSimpleDTO.class);
        videoSimpleDTO.setNumberOfReactions(reactions);
        return videoSimpleDTO;
    }

    public Page<VideoWithoutOwnerDTO> getAllVideos(int userId, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<VideoWithoutOwnerDTO> videoPage = videoRepository.findAllByOwnerId(userId, loggedUserId,pageable)
                .map(v -> mapper.map(v, VideoWithoutOwnerDTO.class));
        if (videoPage.getContent().size() == 0) {
            throw new NotFoundException("No videos found");
        }
        return videoPage;
    }

    public Page<VideoSimpleDTO> getByName(String videoName, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<VideoSimpleDTO> videoPage = videoRepository.findAllContains(videoName, loggedUserId, pageable)
                .map(v -> mapper.map(v, VideoSimpleDTO.class));
        if (videoPage.getContent().size() == 0) {
            throw new NotFoundException("No videos with the given name found.");
        }
        return videoPage;
    }

    public Page<VideoSimpleDTO> getByHashtag(String hashtag, int loggedUserId, int page, int limit) {
        if (!hashtagRepository.existsByTag("#" + hashtag)) {
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        pageable = PageRequest.of(page, limit);
        Page<VideoSimpleDTO> videoPage = videoRepository.findAllNotPrivateVideosByHashtag("#" + hashtag,loggedUserId, pageable)
                .map(v -> mapper.map(v, VideoSimpleDTO.class));
        if (videoPage.getContent().size() == 0) {
            throw new NotFoundException("No videos found");
        }
        return videoPage;
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
        Page<VideoSimpleDTO> videoSimpleDTOS = videos.map(v -> mapper.map(v, VideoSimpleDTO.class));

        if (videoSimpleDTOS.getContent().size() == 0) {
            throw new BadRequestException("No more videos.");
        } else {
            for (int i = 0; i < videoSimpleDTOS.getContent().size(); i++) {
                int reactions = videos.getContent().get(i).getReactions().size();
                int comments = videos.getContent().get(i).getComments().size();
                videoSimpleDTOS.getContent().get(i).setNumberOfReactions(reactions);
                videoSimpleDTOS.getContent().get(i).setNumberOfComments(comments);
            }
        }
        return videoSimpleDTOS;
    }

    public Page<VideoSimpleDTO> getMyLikedVideos(int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<VideoSimpleDTO> videoPage = videoRepository.findAllByReactions(loggedUserId, pageable)
                .map(v -> mapper.map(v, VideoSimpleDTO.class));
        if (videoPage.getContent().size() == 0) {
            throw new NotFoundException("No videos found.");
        }
        return videoPage;
    }
}
