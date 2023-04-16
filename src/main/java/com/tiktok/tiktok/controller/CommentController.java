package com.tiktok.tiktok.controller;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController extends AbstractController{
    @Autowired
    private CommentService commentService;

    @GetMapping("/comments/{commentId}")
    public CommentWithoutVideoAndParentComment getById(@PathVariable int commentId){
        return commentService.getById(commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    public CommentDeletedDTO deleteComment(@PathVariable int commentId, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return commentService.deleteComment(commentId, loggedUserId);
    }

    @PostMapping("/comments/{commentId}/reply")
    public CommentWithoutRepliedDTO replyToComment(@PathVariable int commentId, @RequestBody String text, HttpSession s){
        int loggedUserId = getLoggedUserId(s);
        return commentService.replyToComment(commentId, loggedUserId, text);
    }
    @PostMapping("/comments/{commentId}/react")
    public CommentReactionDTO likeDislike(@PathVariable int commentId, HttpSession s){
        int userId = getLoggedUserId(s);
        return commentService.likeDislike(commentId, userId);
    }
    @GetMapping("/comments/{commentId}/reactions")
    public NumberOfReactionsDTO getReactions(@PathVariable int commentId, HttpSession s){
        int userId = checkIfIsLogged(s);
        return commentService.getReactions(commentId, userId);
    }
}
