package com.tskmgmnt.rhine.repository;

import com.tskmgmnt.rhine.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);

    List<Comment> findByRecipientEmail(String recipientEmail);

    List<Comment> findByTaskIdAndRecipientEmailAndIsReadByRecipientFalse(Long taskId, String recipientEmail);

    long countByTaskIdAndRecipientEmailAndIsReadByRecipientFalse(Long taskId, String recipientEmail);
}

