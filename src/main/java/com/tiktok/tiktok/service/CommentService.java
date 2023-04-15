package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.Comment;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService extends AbstractService{


    public CommentWithoutVideoAndParentComment getById(int commentId) {
        Comment comment = getCommentById(commentId);
        return mapper.map(comment, CommentWithoutVideoAndParentComment.class);
    }


    public CommentDeletedDTO deleteComment(int commentId, int loggedUserId) {
        Comment comment = getCommentById(commentId);
        if (comment.getOwner().getId() != loggedUserId){
            throw new UnauthorizedException("Can't delete this comment. You are unauthorized.");
        }
        commentRepository.deleteById(commentId);
        return mapper.map(comment, CommentDeletedDTO.class);
    }

    public CommentWithoutRepliedDTO replyToComment(int commentId, int loggedUserId, String text) {
        Comment parentComment = getCommentById(commentId);
        User owner = getUserById(loggedUserId);

        Comment comment = new Comment();
        comment.setParentComment(parentComment);
        comment.setVideo(parentComment.getVideo());
        comment.setOwner(owner);
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        return mapper.map(comment, CommentWithoutRepliedDTO.class);
    }
}
