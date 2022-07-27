package com.miniproject.post.service;

import com.miniproject.post.domain.Post;
import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.repository.PostRepository;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import java.io.IOException;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void createPost(String username, PostRequestDto requestDto) throws IOException {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        String uploadImageUrl = s3Uploader.upload(requestDto.getData(), "static");

        Post post = Post.builder()
              .user(user)
              .contents(requestDto.getContents())
              .lat(requestDto.getLat())
              .lng(requestDto.getLng())
              .imageFileName(uploadImageUrl)
              .address(requestDto.getAddress())
              .build();

        postRepository.save(post);
    }


    // 게시글 상세 조회
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

    public void deletePost(Long id, Long username) {
        Long writerId = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")).getUser().getId();
        if (Objects.equals(writerId, username)) {
            postRepository.deleteById(id);
        }

//        if (postRepository.findById(id).isPresent()) {
//            postRepository.deleteById(id);
//        } else {
//            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
//        }

    }


}
