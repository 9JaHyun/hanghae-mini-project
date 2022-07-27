package com.miniproject.post.controller;

import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.service.PostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.miniproject.post.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final S3Uploader s3Uploader;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto requestDto,
          @RequestParam("data") MultipartFile multipartFile,
          @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        postService.createPost(userDetails.getUsername(), requestDto, multipartFile);

        return ResponseEntity.status(HttpStatus.OK)
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
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
         return postService.updatePost(id, requestDto);
    }


    // 이미지 업로드
//    @PostMapping("/images")
//    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
//        s3Uploader.upload(multipartFile, "static");
//        return "test";
//    }

    // 이미지 업로드
//    @PostMapping("/image")
//    public String image(@RequestParam MultipartFile file) {
//
//        UUID uuid = UUID.randomUUID();
//        // .getOriginalFilename 파일이름 찾는 함수
//        String imageFileName = uuid + "_" + file.getOriginalFilename();
//
//        // 사진은 하드에 저장
//        // 파일이름은 db에 저장
//        String path = "C:/Users/HRYUN/Desktop/항해99/week06/hanghae-mini-project/src/main/resources/static/image";
//
//        Path imagePath = Paths.get(path + imageFileName);
//
//        try {
//            Files.write(imagePath, file.getBytes());
//        } catch (Exception e) {
//
//        }
//        return imageFileName;
//    }


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
