package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
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
        }
        videoRepository.deleteById(videoId);
        logger.info("Video deleted successfully: " + videoId);
        return mapper.map(video, VideoDeletedDTO.class);
    }

    public VideoSimpleDTO getById(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        int reactions = video.getReactions().size();
        canWatch(video, loggedUserId);
        logger.info("Video found: " + videoId);
        VideoSimpleDTO videoSimpleDTO = mapper.map(video, VideoSimpleDTO.class);
        videoSimpleDTO.setNumberOfReactions(reactions);
        return videoSimpleDTO;
    }
    public List<VideoWithoutOwnerDTO> getAllVideos(int userId,int loggedUserId, int page, int limit) {
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
        canWatch(video, loggedUserId);
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
        canWatch(video, loggedUserId);
        return video.getReactions().size();
    }

    public List<VideoResponseDTO> showFeed(int loggedUserId, int pageNumber, int videosPerPage) {
        pageable = PageRequest.of(pageNumber, videosPerPage);
        Page<Video> videos = videoRepository.showAllVideosByViews(loggedUserId, pageable);
        List<VideoResponseDTO> videoResponseDTOS = videos.stream()
                .map(v -> mapper.map(v, VideoResponseDTO.class))
                .collect(Collectors.toList());

        if (videoResponseDTOS.size() == 0) {
            throw new BadRequestException("No more videos.");
        } else {
            for (int i = 0; i < videoResponseDTOS.size(); i++) {
                int reactions = videos.getContent().get(i).getReactions().size();
                int comments = videos.getContent().get(i).getComments().size();
                videoResponseDTOS.get(i).setNumberOfReactions(reactions);
                videoResponseDTOS.get(i).setNumberOfComments(comments);
            }
        }
        return videoResponseDTOS;
    }
}
