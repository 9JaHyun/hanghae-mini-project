package com.miniproject.controller;

import com.miniproject.dto.PostRequestDto;
import com.miniproject.model.Post;
import com.miniproject.repository.PostRepository;
import com.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    // 게시글 작성
    @PostMapping("/posts/")
    public void createPost(@RequestBody PostRequestDto requestDto//, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
//        String username = userDetails.getUser().getUsername();
        postService.createPost(requestDto);
    }

    // 개별 게시글 상세 조회
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("글이 존재하지 않습니다")
        );

        return post;
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
         postService.updatePost(id, requestDto);

         return id;
    }

    // 이미지 업로드
    @PostMapping("/image")
    public @ResponseBody String image(@RequestParam MultipartFile file) {

        UUID uuid = UUID.randomUUID();
        // .getOriginalFilename 파일이름 찾는 함수
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        // 사진은 하드에 저장
        // 파일이름은 db에 저장
        String path = "C:/Users/HRYUN/Desktop/항해99/week06/hanghae-mini-project/src/main/resources/static/image";

        Path imagePath = Paths.get(path + imageFileName);

        try {
            Files.write(imagePath, file.getBytes());
        } catch (Exception e) {

        }
        return imageFileName;
    }


    // 게시글 삭제
    @DeleteMapping("posts/{id}")
    public Long deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return id;

    }

    // 게시글 삭제 (jwt 적용)
//    @DeleteMapping("posts/{id}")
//    public Long deletePost(@PathVariable Long id//, @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        if (userDetails != null) {
//            String username = userDetails.getUser().getUsername();
//            String result = postService.deletePost(id);
//            return result;
//
//        }
}
