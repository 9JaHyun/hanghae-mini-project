package com.miniproject.post.controller;

import com.miniproject.post.domain.Post;
import com.miniproject.post.dto.PostListResponseDto;
import com.miniproject.post.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostListController {

    private final PostListService postListService;

    // 보여지는 게시글 개수 지정
    public static final int PageSize = 12;

    // 최초 메인페이지 컨트롤러
    @GetMapping("/")
    // sort = "id", direction = Sort.Direction.DESC 아이디로 내림차순 정렬
    public List<PostListResponseDto> firstMainList(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = PageSize) Pageable pageable) {
        // 응답 list 객체 생성
        List<PostListResponseDto> data = new ArrayList<>(); //
        Page<Post> list = postListService.firstMainList(pageable);
        for (int i = 0; i < PageSize; i++) {
            data.add(new PostListResponseDto(list.getContent().get(i)));
        }
        System.out.println(data);
        return data;
    }

    // 메인페이지에서 무한스크롤 발생시 반응하는 컨트롤러 - 현재 마지막 작품 id 와 size 값을 받아 데이터 가공
    // sort = "id", direction = Sort.Direction.DESC 아이디로 내림차순 정렬
    @GetMapping("/posts")
    public List<PostListResponseDto> mainInfinity(@RequestParam Long lastPostId,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = PageSize) Pageable pageable){

        // 응답 list 객체 생성
        List<PostListResponseDto> data = new ArrayList<>();
        Page<Post> list = postListService.mainInfinity(lastPostId, pageable);
        for (int i = 0; i < PageSize; i++){
            data.add(new PostListResponseDto(list.getContent().get(i)));
        }
        return data;
    }
}
