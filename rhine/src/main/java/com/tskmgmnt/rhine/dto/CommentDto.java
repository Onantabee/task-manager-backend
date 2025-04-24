package com.tskmgmnt.rhine.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private Long id;
    private String content;
    private String authorEmail;
    private String recipientEmail;
    private Long taskId;
    private boolean isReadByRecipient;
    private LocalDateTime createdAt;

    public CommentDto(Long taskId, boolean isReadByRecipient) {
        this.taskId = taskId;
        this.isReadByRecipient = isReadByRecipient;
    }

    public CommentDto(Long id, String content, String authorEmail, String recipientEmail, boolean isReadByRecipient, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorEmail = authorEmail;
        this.recipientEmail = recipientEmail;
        this.isReadByRecipient = isReadByRecipient;
        this.createdAt = createdAt;
    }

    public CommentDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public boolean isReadByRecipient() {
        return isReadByRecipient;
    }

    public void setReadByRecipient(boolean readByRecipient) {
        isReadByRecipient = readByRecipient;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
