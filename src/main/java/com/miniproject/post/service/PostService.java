package com.miniproject.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.model.Image;
import com.miniproject.post.model.Post;
import com.miniproject.post.repository.ImageRepository;
import com.miniproject.post.repository.PostRepository;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void createPost(String username, PostRequestDto requestDto, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        Image uploadImage = s3Uploader.upload(multipartFile, "static");


        Post post = Post.builder()
              .user(user)
              .contents(requestDto.getContents())
              .imageFileName(requestDto.getImageFileName())
              .lat(requestDto.getLat())
              .lng(requestDto.getLng())
              .address(requestDto.getAddress())
              .build();

        uploadImage.setPost(post);
        // TODO 사진 저장 로직 추가

        imageRepository.save(uploadImage);

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

    public void deletePost(Long id) {
        if (postRepository.findById(id).isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }

}
