package com.tskmgmnt.rhine.repositories;

import com.tskmgmnt.rhine.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
