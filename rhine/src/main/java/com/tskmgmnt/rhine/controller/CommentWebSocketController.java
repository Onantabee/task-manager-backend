package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.CommentReq;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class CommentWebSocketController {

    @MessageMapping("/comment")
    @SendTo("/topic/comments")
    public CommentReq sendMessage(CommentReq comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        return comment;
    }
}
