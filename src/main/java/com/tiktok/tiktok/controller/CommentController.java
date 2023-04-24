package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.commentsDTOs.*;
import com.tiktok.tiktok.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/videos/{videoId}/comment")
    public ResponseEntity<CommentWithIdOwnerVideoDTO> addComment(@PathVariable int videoId, @RequestBody CommentAddDTO dto, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentWithIdOwnerVideoDTO commentFullInfoDTO = commentService.addComment(videoId, loggedUserId, dto.getComment());
        return ResponseEntity.ok(commentFullInfoDTO);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentWithIdOwnerRepliedDTO> getById(@PathVariable int commentId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentWithIdOwnerRepliedDTO comment = commentService.getById(commentId, loggedUserId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/videos/{videoId}/comments")
    public ResponseEntity<Page<CommentWithIdOwnerRepliedDTO>> getAllComments(@PathVariable int videoId, @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "4") int limit, HttpSession s) {
        int loggedUserId = checkIfIsLogged(s);
        Page<CommentWithIdOwnerRepliedDTO> commentWithIdOwnerParentDTOS = commentService.getAllComments(videoId, loggedUserId, page, limit);
        return ResponseEntity.ok(commentWithIdOwnerParentDTOS);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentDeletedDTO> deleteComment(@PathVariable int commentId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentDeletedDTO commentDeletedDTO = commentService.deleteComment(commentId, loggedUserId);
        return ResponseEntity.ok(commentDeletedDTO);
    }

    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<CommentWithIdOwnerParentDTO> replyToComment(@PathVariable int commentId, @RequestBody CommentAddDTO dto, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentWithIdOwnerParentDTO comment = commentService.replyToComment(commentId, loggedUserId, dto.getComment());
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/comments/{commentId}/react")
    public ResponseEntity<CommentReactionDTO> likeDislike(@PathVariable int commentId, HttpSession s) {
        int loggedUserId = getLoggedUserId(s);
        CommentReactionDTO comment = commentService.likeDislike(commentId, loggedUserId);
        return ResponseEntity.ok(comment);
    }
}
