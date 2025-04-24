package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.CommentDto;
import com.tskmgmnt.rhine.dto.CommentUpdateDto;
import com.tskmgmnt.rhine.dto.NotificationDto;
import com.tskmgmnt.rhine.entity.Comment;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.repository.CommentRepository;
import com.tskmgmnt.rhine.repository.TaskRepository;
import com.tskmgmnt.rhine.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final SimpMessagingTemplate messagingTemplate;
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(SimpMessagingTemplate messagingTemplate, CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getEmail(),
                comment.getRecipient() != null ? comment.getRecipient().getEmail() : null,
                comment.isReadByRecipient(),
                comment.getCreatedAt()
        );
        dto.setTaskId(comment.getTask().getId());
        return dto;
    }

    public List<CommentDto> getCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    public List<Comment> getCommentsByRecipient(String recipientEmail) {
        return commentRepository.findByRecipientEmail(recipientEmail);
    }

    public Comment addComment(Long taskId, String authorEmail, String content, String recipientEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));

        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author email"));

        User recipient = (recipientEmail != null) ?
                userRepository.findByEmail(recipientEmail).orElse(null) : null;

        Comment comment = new Comment(content, author, task, recipient);

        Comment savedComment = commentRepository.save(comment);

        CommentDto commentDto = mapToDto(savedComment);

        messagingTemplate.convertAndSend("/topic/comments", commentDto);

        Set<String> notifyEmails = new HashSet<>();
        if (recipient != null) {
            notifyEmails.add(recipient.getEmail());
        }
        notifyEmails.add(task.getCreatedBy().getEmail());

        for (String email : notifyEmails) {
            long count = countUnreadCommentsByRecipient(email, taskId);
            messagingTemplate.convertAndSend(
                    "/topic/unread-count/" + email + "/" + taskId,
                    Map.of("count", count)
            );
        }

        return savedComment;
    }

    public void markCommentAsRead(Long commentId, String userEmail) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getRecipient() != null &&
                    comment.getRecipient().getEmail().equals(userEmail)) {
                comment.setReadByRecipient(true);
                commentRepository.save(comment); // Explicit save
                messagingTemplate.convertAndSend("/topic/comments-read",
                        new NotificationDto<>("COMMENTS_READ", mapToDto(comment)));
            }
        }
    }

    public void markCommentsAsReadByRecipient(Long taskId, String recipientEmail) {
        List<Comment> recipientComments = commentRepository.findByTaskIdAndRecipientEmailAndIsReadByRecipientFalse(taskId, recipientEmail);
        recipientComments.forEach(comment -> {
            comment.setReadByRecipient(true);
            commentRepository.save(comment);
            messagingTemplate.convertAndSend("/topic/comments-read-by-recipient",
                    new NotificationDto<>("COMMENTS_READ_BY_RECIPIENTS", mapToDto(comment)));
        });
    }

    public long countUnreadCommentsByRecipient(String recipientEmail, Long taskId) {
        return commentRepository.countByTaskIdAndRecipientEmailAndIsReadByRecipientFalse(taskId, recipientEmail);
    }

    public Comment updateCommentById(Long id, CommentUpdateDto commentUpdateDto) {
        Comment existingComment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not Found!"));
        existingComment.setContent(commentUpdateDto.getContent());
        Comment updatedComment = commentRepository.save(existingComment);

        messagingTemplate.convertAndSend("/topic/comment-update", mapToDto(updatedComment));
        return commentRepository.save(existingComment);
    }

    public Comment deleteCommentById(Long id) {
        Comment commentToDelete = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(commentToDelete);
        return commentToDelete;
    }
}
