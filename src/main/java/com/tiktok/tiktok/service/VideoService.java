package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.CommentSimpleDTO;
import com.tiktok.tiktok.model.DTOs.CommentWithoutVideoDTO;
import com.tiktok.tiktok.model.DTOs.VideoDeletedDTO;
import com.tiktok.tiktok.model.DTOs.VideoSimpleDTO;
import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public VideoSimpleDTO getById(int videoId) {
        Video video = getVideoById(videoId);
        return mapper.map(video, VideoSimpleDTO.class);
    }

    public List<CommentWithoutVideoDTO> getAllComments(int videoId) {
        Video video = getVideoById(videoId);
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
        User owner = getUserById(loggedUserId);

        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setOwner(owner);
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return mapper.map(comment, CommentSimpleDTO.class);
    }
}
