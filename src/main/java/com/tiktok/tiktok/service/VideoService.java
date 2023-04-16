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
public class VideoService extends AbstractService{

    @Autowired
    private VideoReactionRepository videoReactionRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public VideoDeletedDTO deleteVideo(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (video.getOwner().getId() != loggedUserId){
            throw new UnauthorizedException("Can't delete this video. You are unauthorized.");
        }
        videoRepository.deleteById(videoId);
        return mapper.map(video, VideoDeletedDTO.class);
    }

    public VideoSimpleDTO getById(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video, loggedUserId)) {
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        return mapper.map(video, VideoSimpleDTO.class);
    }

    public List<CommentWithoutVideoDTO> getAllComments(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video, loggedUserId)){
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        List<Comment> comments = video.getComments();
        if (comments.size() == 0){
            throw new NotFoundException("No comments found");
        }
        return comments.stream()
                .map(comment -> mapper.map(comment, CommentWithoutVideoDTO.class))
                .collect(Collectors.toList());
    }


    public CommentSimpleDTO addComment(int videoId, int loggedUserId, String comment) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video,loggedUserId)){
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        User owner = getUserById(loggedUserId);

        Comment c = new Comment();
        c.setVideo(video);
        c.setOwner(owner);
        c.setComment(comment.trim());
        c.setCreatedAt(LocalDateTime.now());

        commentRepository.save(c);

        return mapper.map(c, CommentSimpleDTO.class);
    }

    public List<VideoSimpleDTO> getByName(String videoName, int loggedUserId) {
        List<Video> videos = videoRepository.findAllContains(videoName);
        if (videos.size() == 0){
            throw new NotFoundException("No videos with the given name found.");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos){
            if (isPossibleToWatch(v,loggedUserId)){
                videosNotPrivate.add(v);
            }
        }
        if (videosNotPrivate.size() == 0){
            throw new NotFoundException("No videos with the given name found.");
        }
        return videosNotPrivate.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> getByHashtag(String hashtag, int loggedUserId) {
        if (!hashtagRepository.existsByTag("#" + hashtag)){
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        Set<Video> videos = hashtagRepository.findByTag("#" + hashtag).getVideo();
        if (videos.size() == 0){
            throw new NotFoundException("No videos with the given hashtag found");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos){
            if (isPossibleToWatch(v, loggedUserId)){
                videosNotPrivate.add(v);
            }
        }
        if (videosNotPrivate.size() == 0){
            throw new NotFoundException("No videos with the given hashtag found");
        }
        return videosNotPrivate.stream()
                .map(v -> mapper.map(v, VideoSimpleDTO.class))
                .collect(Collectors.toList());
    }

    public VideoReactionDTO likeDislike(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        User user = getUserById(loggedUserId);
        Optional<VideoReactions> videoReactions = videoReactionRepository.findByVideoAndUser(video, user);
        if (videoReactions.isPresent()){
            VideoReactions reactions1 = videoReactions.get();
            reactions1.setLiked(!reactions1.isLiked());
            videoReactionRepository.save(reactions1);
            return mapper.map(reactions1, VideoReactionDTO.class);
        } else{
            VideoReactions reactions = new VideoReactions();
            reactions.setUser(user);
            reactions.setVideo(video);
            reactions.setLiked(true);
            videoReactionRepository.save(reactions);
            return mapper.map(reactions, VideoReactionDTO.class);
        }
    }

    public NumberOfReactionsDTO getReactions(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video,loggedUserId)){
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        NumberOfReactionsDTO dto = new NumberOfReactionsDTO();
        dto.setReactions(video.getReactions().size());
        return dto;
    }
}
