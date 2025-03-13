package com.tskmgmnt.rhine.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "task")
    @JsonBackReference
    private Task task;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonBackReference
    private User createdBy;

    public Comment() {

    }

    public Comment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

