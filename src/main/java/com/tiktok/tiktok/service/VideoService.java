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
        Video video = getVideoById(videoId);
        int reactions = video.getReactions().size();
        isPossibleToWatch(video, loggedUserId);
        logger.info("Video found: " + videoId);
        VideoSimpleDTO videoSimpleDTO = mapper.map(video, VideoSimpleDTO.class);
        videoSimpleDTO.setNumberOfReactions(reactions);
        return videoSimpleDTO;
    }

    public List<VideoWithoutOwnerDTO> getAllVideos(int userId, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<Video> videoPage;
        if (userId == loggedUserId){
            videoPage = videoRepository.findAllByOwnerId(loggedUserId,pageable);
        }
        else {
            videoPage = videoRepository.showAllVideosCreatedAt(userId, pageable);
        }
        List<Video> videos = videoPage.getContent();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos found");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoWithoutOwnerDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> getByName(String videoName, int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit);
        Page<Video> videoPage = videoRepository.findAllContains(videoName, loggedUserId, pageable);
        List<Video> videos = videoPage.getContent();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos with the given name found.");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> getByHashtag(String hashtag, int loggedUserId, int page, int limit) {
        if (!hashtagRepository.existsByTag("#" + hashtag)) {
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        pageable = PageRequest.of(page, limit);
        Page<Video> videoPage = videoRepository.findAllNotPrivateVideosByHashtag("#" + hashtag,loggedUserId, pageable);
        List<Video> videos = videoPage.getContent();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos found");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
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

    public List<VideoSimpleDTO> showFeed(int loggedUserId, int pageNumber, int videosPerPage) {
        pageable = PageRequest.of(pageNumber, videosPerPage, Sort.by("created_at").descending());
        Page<Video> videos = videoRepository.showAllVideosByViews(loggedUserId, pageable);
        List<VideoSimpleDTO> videoSimpleDTOS = videos.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());

        if (videoSimpleDTOS.size() == 0) {
            throw new BadRequestException("No more videos.");
        } else {
            for (int i = 0; i < videoSimpleDTOS.size(); i++) {
                int reactions = videos.getContent().get(i).getReactions().size();
                int comments = videos.getContent().get(i).getComments().size();
                videoSimpleDTOS.get(i).setNumberOfReactions(reactions);
                videoSimpleDTOS.get(i).setNumberOfComments(comments);
            }
        }
        return videoSimpleDTOS;
    }

    public List<VideoSimpleDTO> getMyLikedVideos(int loggedUserId, int page, int limit) {
        pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        Page<Video> videoPage = videoRepository.findAllByReactions(loggedUserId, pageable);
        List<Video> videos = videoPage.getContent();
        if (videos.size() == 0) {
            throw new NotFoundException("No videos found.");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }
}
