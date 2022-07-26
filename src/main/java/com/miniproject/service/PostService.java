package com.miniproject.service;

import com.miniproject.dto.PostRequestDto;
import com.miniproject.model.Post;
import com.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    @Transactional
    public void createPost(PostRequestDto requestDto) {

        Post post = Post.builder()
                .nickname(requestDto.getNickname())
                .contents(requestDto.getContents())
                .imageFileName(requestDto.getImageFileName())
                .lat(requestDto.getLat())
                .lng(requestDto.getLng())
                .address(requestDto.getAddress())
                .build();

        postRepository.save(post);

    }

    @Transactional
    public Long updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        post.update(requestDto);

        return post.getId();




    }

    public void deletePost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );

        postRepository.deleteById(id);
    }

}
