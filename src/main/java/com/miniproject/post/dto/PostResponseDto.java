package com.miniproject.post.dto;

import com.miniproject.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private String nickname;
    private String contents;
    private String imageFileName;
    private String lat;
    private String lng;
    private String address;

    public PostResponseDto(Post post) {
        this.nickname = post.getUser().getNickname();
        this.nickname = post.getUser().getProfile();
        this.contents = post.getContents();
        this.imageFileName = post.getImageFileName();
        this.lat = post.getLat();
        this.lng = post.getLng();
        this.address = post.getAddress();
    }
}
