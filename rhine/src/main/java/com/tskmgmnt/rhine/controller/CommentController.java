package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.CommentDto;
import com.tskmgmnt.rhine.dto.CommentUpdateDto;
import com.tskmgmnt.rhine.dto.MarkAsReadDto;
import com.tskmgmnt.rhine.entity.Comment;
import com.tskmgmnt.rhine.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*")
@Tag(
        name = "CRUD REST APIs for In-Task Messaging",
        description = "CRUD REST APIs - Publish Comment By Task Id, Get Comments By Task Id, " +
                "Get Comments By Recipient Id, Get Unread Comment Count, " +
                "Update Comment Read State By Recipient Id, Update All Comment Read State"
)
public class CommentController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;

    public CommentController(CommentService commentService, SimpMessagingTemplate messagingTemplate) {
        this.commentService = commentService;
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(
            summary = "Get comments by task ID",
            description = "Retrieves all comments associated with a specific task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @GetMapping("/task/{taskId}")
    public List<CommentDto> getCommentsByTask(@PathVariable Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }

    @Operation(
            summary = "Get comments by recipient email",
            description = "Retrieves all comments where the specified user is the recipient",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
                    @ApiResponse(responseCode = "404", description = "Recipient not found")
            }
    )
    @GetMapping("/recipient/{recipientEmail}")
    public List<Comment> getCommentsByRecipient(@PathVariable String recipientEmail) {
        return commentService.getCommentsByRecipient(recipientEmail);
    }

    @Operation(
            summary = "Add a new comment to a task",
            description = "Creates a new comment on the specified task and notifies the recipient",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created comment"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task or user not found")
            }
    )
    @PostMapping("/task/{taskId}")
    public Comment addComment(@PathVariable Long taskId, @RequestBody Map<String, String> payload) {
        return commentService.addComment(
                taskId,
                payload.get("authorEmail"),
                payload.get("content"),
                payload.get("recipientEmail")
        );
    }

    @Operation(
            summary = "Mark comments as read by recipient",
            description = "Marks all comments for a specific task and recipient as read, and broadcasts the updated unread count via WebSocket",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully marked comments as read"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task or recipient not found")
            }
    )
    @PostMapping("/mark-as-read-by-recipient/{taskId}")
    public void markCommentsAsReadByRecipients(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> payload
    ) {
        String recipientEmail = payload.get("recipientEmail");
        commentService.markCommentsAsReadByRecipient(taskId, recipientEmail);

        long newCount = commentService.countUnreadCommentsByRecipient(recipientEmail, taskId);
        messagingTemplate.convertAndSend(
                "/topic/unread-updates",
                Map.of("taskId", taskId, "count", newCount, "recipientEmail", recipientEmail)
        );
    }

    @Operation(
            summary = "Count unread comments by recipient",
            description = "Returns the count of unread comments for a specific recipient in a task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved unread count"),
                    @ApiResponse(responseCode = "404", description = "Task or recipient not found")
            }
    )
    @GetMapping("/count-unread-by-recipient/{taskId}/{recipientEmail}")
    public long countUnreadCommentsByRecipients(
            @PathVariable Long taskId,
            @PathVariable String recipientEmail
    ) {
        return commentService.countUnreadCommentsByRecipient(recipientEmail, taskId);
    }

    @Operation(
            summary = "Mark a single comment as read",
            description = "Marks a specific comment as read by a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully marked comment as read"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Comment or user not found")
            }
    )
    @PostMapping("/mark-as-read/{commentId}")
    public void markCommentAsRead(
            @PathVariable Long commentId,
            @RequestBody MarkAsReadDto markAsReadDto) {
        commentService.markCommentAsRead(commentId, markAsReadDto.getUserEmail());
    }

    @Operation(
            summary = "Update a comment by ID",
            description = "Updates the content of an existing comment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated comment"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @PutMapping("/{commentId}")
    public Comment updateCommentById(@PathVariable Long commentId, @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.updateCommentById(commentId, commentUpdateDto);
    }

    @Operation(
            summary = "Delete a comment by ID",
            description = "Deletes a specific comment from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @DeleteMapping("/{commentId}")
    public Comment deleteCommentById(@PathVariable Long commentId) {
        return commentService.deleteCommentById(commentId);
    }
}