package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.CommentReq;
import com.tskmgmnt.rhine.entity.Comment;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.repository.CommentRepository;
import com.tskmgmnt.rhine.repository.TaskRepository;
import com.tskmgmnt.rhine.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<CommentReq> getCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(comment -> new CommentReq(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor().getEmail(),
                        comment.getCreatedAt()
                ))
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

        Comment savedComment = commentRepository.save(new Comment(content, author, task, recipient));

        CommentReq commentReq = new CommentReq(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getAuthor().getEmail(),
                savedComment.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/comments", commentReq);

        return savedComment;
    }

//    public void deleteComment(Long commentId, Long userId, boolean isAdmin) {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
//
//        if (!isAdmin && !comment.getAuthor().getEmail().equals(userId)) {
//            throw new SecurityException("User not authorized to delete this comment");
//        }
//
//        commentRepository.delete(comment);
//    }
}
