package com.miniproject.post.dto;

import com.miniproject.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostListResponseDto {

    private Long id;
    private String contents;
    private String imageFileName;
    private String lat;
    private String lng;
    private String address;
    private String uploadImageUrl;


    public PostListResponseDto(Post post) {

        this.id = post.getId();
        this.contents = post.getContents();
        this.imageFileName = post.getImageFileName();
        this.lat = post.getLat();
        this.lng = post.getLng();
        this.address = post.getAddress();
        this.uploadImageUrl = post.getImageFileName();
    }


}
