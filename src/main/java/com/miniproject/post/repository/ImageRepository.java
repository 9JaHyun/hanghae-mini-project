package com.miniproject.post.repository;

import com.miniproject.post.model.Image;
import com.miniproject.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
