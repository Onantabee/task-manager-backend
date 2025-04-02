package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.CommentDto;
import com.tskmgmnt.rhine.entity.Comment;
import com.tskmgmnt.rhine.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/task/{taskId}")
    public List<CommentDto> getCommentsByTask(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }

    @GetMapping("/recipient/{recipientEmail}")
    public List<Comment> getCommentsByRecipient(@PathVariable String recipientEmail) {
        return commentService.getCommentsByRecipient(recipientEmail);
    }

    @PostMapping("/task/{taskId}")
    public Comment addComment(@PathVariable Long taskId, @RequestBody Map<String, String> payload) {
        return commentService.addComment(
                taskId,
                payload.get("authorEmail"),
                payload.get("content"),
                payload.get("recipientEmail")
        );
    }

//    @DeleteMapping("/{commentId}")
//    public ResponseEntity<?> deleteComment(
//            @PathVariable Long commentId,
//            @RequestParam Long userId,
//            @RequestParam boolean isAdmin) {
//
//        commentService.deleteComment(commentId, userId, isAdmin);
//        return ResponseEntity.ok(Map.of("message", "Comment deleted successfully"));
//    }
}
