package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.Sound;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.modelmapper.Converters.Collection.map;

@Service
public class VideoService extends AbstractService{

    public VideoDeletedDTO deleteVideo(int videoId, int loggedUserId) {
        Video video = getVideoById(videoId);
        if (video.getOwner().getId() != loggedUserId){
            throw new UnauthorizedException("Can't delete this video. You are unauthorized.");
        }
        videoRepository.deleteById(videoId);
        return mapper.map(video, VideoDeletedDTO.class);
    }

    public VideoSimpleDTO getById(int videoId, int userId) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video, userId)) {
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        return mapper.map(video, VideoSimpleDTO.class);
    }

    public List<CommentWithoutVideoDTO> getAllComments(int videoId, int userId) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video, userId)){
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


    public CommentSimpleDTO addComment(int videoId, int loggedUserId, String text) {
        Video video = getVideoById(videoId);
        if (!isPossibleToWatch(video,loggedUserId)){
            throw new UnauthorizedException("This video is private and you do not have access to it.");
        }
        User owner = getUserById(loggedUserId);

        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setOwner(owner);
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return mapper.map(comment, CommentSimpleDTO.class);
    }

    public List<VideoSimpleDTO> getByName(String videoName, int userId) {
        List<Video> videos = videoRepository.findAllContains(videoName);
        if (videos.size() == 0){
            throw new NotFoundException("No videos with the given name found.");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos){
            if (isPossibleToWatch(v,userId)){
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

    public List<VideoSimpleDTO> getByHashtag(String hashtag, int userId) {
        if (!hashtagRepository.existsByTag("#" + hashtag)){
            throw new NotFoundException("No results found for #" + hashtag + "");
        }
        Set<Video> videos = hashtagRepository.findByTag("#" + hashtag).getVideo();
        if (videos.size() == 0){
            throw new NotFoundException("No videos with the given hashtag found");
        }
        List<Video> videosNotPrivate = new ArrayList<>();
        for (Video v : videos){
            if (isPossibleToWatch(v, userId)){
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
}
