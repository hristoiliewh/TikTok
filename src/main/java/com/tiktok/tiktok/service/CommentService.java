package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.commentsDTOs.*;
import com.tiktok.tiktok.model.entities.*;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import com.tiktok.tiktok.model.repositories.CommentReactionRepository;
import com.tiktok.tiktok.model.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService {
    @Autowired
    private CommentReactionRepository commentReactionRepository;

    @Autowired
    protected CommentRepository commentRepository;

    public CommentWithIdOwnerVideoDTO addComment(int videoId, int loggedUserId, String comment) {
        Video video = getVideoById(videoId);
        isPossibleToWatch(video, loggedUserId);
        User owner = getUserById(loggedUserId);
        Comment c = new Comment();
        c.setVideo(video);
        c.setOwner(owner);
        c.setComment(comment.trim());
        c.setCreatedAt(LocalDateTime.now());
        commentRepository.save(c);
        logger.info("Comment added successfully by user with ID " + loggedUserId + " to video with ID " + videoId);
        return mapper.map(c, CommentWithIdOwnerVideoDTO.class);
    }

    public CommentWithIdOwnerReplied getById(int commentId, int loggedUserId) {
        Comment comment = getCommentById(commentId);
        if (comment.getOwner().getId() != loggedUserId) {
            logger.error("Unauthorized attempt to access comment with id: {}", commentId);
            throw new UnauthorizedException("Can't delete this comment. You are unauthorized.");
        }
        return mapper.map(comment, CommentWithIdOwnerReplied.class);
    }

    public Page<CommentWithIdOwnerParentDTO> getAllComments(int videoId, int loggedUserId, int page, int limit) {
        Video video = getVideoById(videoId);
        isPossibleToWatch(video, loggedUserId);
        pageable = PageRequest.of(page, limit);
        Page<CommentWithIdOwnerParentDTO> comments = commentRepository.findAllByVideoIdAndCreatedAt(pageable, videoId)
                .map(c -> mapper.map(c, CommentWithIdOwnerParentDTO.class));
        if (comments.getContent().size() == 0) {
            throw new NotFoundException("No comments found");
        }
        return comments;
    }

    public CommentDeletedDTO deleteComment(int commentId, int loggedUserId) {
        Comment comment = getCommentById(commentId);
        if (comment.getOwner().getId() != loggedUserId) {
            throw new UnauthorizedException("Can't delete this comment. You are unauthorized.");
        }
        logger.info("Deleting comment with id: {}", commentId);
        commentRepository.deleteById(commentId);
        return mapper.map(comment, CommentDeletedDTO.class);
    }

    public CommentWithIdOwnerParentDTO replyToComment(int commentId, int loggedUserId, String text) {
        Comment parentComment = getCommentById(commentId);
        User owner = getUserById(loggedUserId);

        Comment comment = new Comment();
        comment.setParentComment(parentComment);
        comment.setVideo(parentComment.getVideo());
        comment.setOwner(owner);
        comment.setComment(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        logger.info("Replying to comment with id: {}", commentId);
        return mapper.map(comment, CommentWithIdOwnerParentDTO.class);
    }

    public CommentReactionDTO likeDislike(int commentId, int userId) {
        logger.info("Liking/disliking comment with id: {}", commentId);
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);
        Optional<CommentReactions> commentReactions = commentReactionRepository.findByCommentAndUser(comment, user);
        if (commentReactions.isPresent()) {
            CommentReactions reactions1 = commentReactions.get();
            reactions1.setLiked(!reactions1.isLiked());
            commentReactionRepository.delete(reactions1);
            return mapper.map(reactions1, CommentReactionDTO.class);
        } else {
            CommentReactions reactions = new CommentReactions();
            reactions.setUser(user);
            reactions.setComment(comment);
            reactions.setLiked(true);
            commentReactionRepository.save(reactions);
            return mapper.map(reactions, CommentReactionDTO.class);
        }
    }

    public int getReactions(int commentId, int userId) {
        Comment comment = getCommentById(commentId);
        Video video = getVideoById(comment.getVideo().getId());
        isPossibleToWatch(video, userId);
        return comment.getReactions().size();
    }

    private Comment getCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));
    }
}
