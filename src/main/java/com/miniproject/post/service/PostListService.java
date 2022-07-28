package com.miniproject.post.service;

import com.miniproject.post.domain.Post;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.repository.PostListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostListService {

    private final PostListRepository postListRepository;

    public Page<Post> firstMainList(Pageable pageable) {

        return postListRepository.findAll(pageable);
    }

    public List<PostResponseDto> mainInfinity(Long lastPostId, Pageable pageable) {
        return postListRepository.findAllByIdLessThan(lastPostId, pageable)
              .map(PostResponseDto::new)
              .toList();

    }
}
