package com.miniproject.comment.repository;

import com.miniproject.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);
}
