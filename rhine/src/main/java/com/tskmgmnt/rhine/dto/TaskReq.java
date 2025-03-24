package com.tskmgmnt.rhine.dto;

import com.tskmgmnt.rhine.enums.TaskStatus;

import java.util.Date;

public class TaskReq {
    private final Long id;
    private final String title;
    private final String description;
    private final Date dueDate;
    private final String priority;
    private final TaskStatus taskStatus;
    private final String createdById;
    private final String assigneeId;

    public TaskReq(Long id, String title, String description, Date dueDate, String priority, TaskStatus taskStatus, String createdById, String assigneeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.createdById = createdById;
        this.assigneeId = assigneeId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getCreatedById() {
        return createdById;
    }

    public String getAssigneeId() { // Change from Set<User> to Long
        return assigneeId;
    }



//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        TaskReq taskReq = (TaskReq) o;
//        return Objects.equals(title, taskReq.title) &&
//                Objects.equals(description, taskReq.description) &&
//                Objects.equals(dueDate, taskReq.dueDate) &&
//                Objects.equals(priority, taskReq.priority) &&
//                taskStatus == taskReq.taskStatus &&
//                Objects.equals(createdById, taskReq.createdById) &&
//                Objects.equals(assigneeId, taskReq.assigneeId); // Change from Set<User> to Long
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(title, description, dueDate, priority, taskStatus, createdById, assigneeId); // Change from Set<User> to Long
//    }
//
//    @Override
//    public String toString() {
//        return "TaskReq{" +
//                "title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", dueDate=" + dueDate +
//                ", priority='" + priority + '\'' +
//                ", taskStatus=" + taskStatus +
//                ", createdById='" + createdById + '\'' +
//                ", assigneeId=" + assigneeId + // Change from Set<User> to Long
//                '}';
//    }
}