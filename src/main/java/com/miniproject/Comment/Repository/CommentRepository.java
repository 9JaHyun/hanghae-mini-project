package com.miniproject.Comment.Repository;

import com.miniproject.Comment.domain.Comment;
import com.miniproject.Comment.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPost(Long postId);
}
