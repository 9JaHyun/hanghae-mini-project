package com.miniproject.Comment.Repository;

import com.miniproject.Comment.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
