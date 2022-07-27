package com.miniproject.post.repository;

import com.miniproject.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostListRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIdLessThan(Long lastPostId, Pageable pageable);
}
