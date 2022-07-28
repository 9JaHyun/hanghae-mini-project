package com.miniproject.post.controller;

import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.service.PostService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(PostRequestDto requestDto,
          @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        postService.createPost(userDetails.getUsername(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body(null);
    }

    // 개별 게시글 상세 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto dto = postService.findById(id);

        return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_JSON)
              .body(dto);
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id,
          PostRequestDto requestDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            Long modifiedId = postService.updatePost(id, requestDto, userDetails.getUser().getId());
            return ResponseEntity.status(HttpStatus.OK)
                  .body(modifiedId);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    @DeleteMapping("posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            Long username = userDetails.getUser().getId();
            postService.deletePost(id, username);
        }
        return ResponseEntity.status(HttpStatus.OK)
              .body(null);
    }
}
