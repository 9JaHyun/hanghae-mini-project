package com.miniproject.post.service;

import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.model.Post;
import com.miniproject.post.repository.PostRepository;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(String username, PostRequestDto requestDto) {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        Post post = Post.builder()
              .user(user)
              .contents(requestDto.getContents())
              .imageFileName(requestDto.getImageFileName())
              .lat(requestDto.getLat())
              .lng(requestDto.getLng())
              .address(requestDto.getAddress())
              .build();

        // TODO 사진 저장 로직 추가

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post postEntity = postRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다"));

        return new PostResponseDto(postEntity);
    }

    @Transactional
    public Long updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        post.update(requestDto);

        return post.getId();
    }

    public void deletePost(Long id) {
        if (postRepository.findById(id).isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }

}
