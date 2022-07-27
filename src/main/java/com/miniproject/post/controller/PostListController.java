package com.miniproject.post.controller;

import com.miniproject.post.dto.PostResponseDto;
import com.miniproject.post.service.PostListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostListController {

    private final PostListService postListService;

    // 보여지는 게시글 개수 지정
    public static final int DEFAULT_PAGE_SIZE = 12;

    // 최초 메인페이지 컨트롤러
    @GetMapping("/posts")
    // sort = "id", direction = Sort.Direction.DESC 아이디로 내림차순 정렬
    public ResponseEntity<List<PostResponseDto>> firstMainList(@RequestParam(defaultValue = ""+Long.MAX_VALUE) Long lastPostId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        // 응답 list 객체 생성
        List<PostResponseDto> result = postListService.mainInfinity(lastPostId, pageable)
              .map(PostResponseDto::new)
              .toList();
        return ResponseEntity.status(HttpStatus.OK)
              .body(result);
    }
}
