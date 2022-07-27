package com.miniproject.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miniproject.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String contents;
    private String imageFileName;

    @JsonProperty(value = "writer")
    private PostUserInfo userInfo;

    private String lat;
    private String lng;
    private String address;
    private String uploadImageUrl;

    public PostResponseDto(Post post) {

        this.id = post.getId();
        this.contents = post.getContents();
        this.userInfo = new PostUserInfo(post.getUser().getNickname(),
              post.getUser().getUsername(),
              post.getUser().getProfile());
        this.lat = post.getLat();
        this.imageFileName = post.getImageFileName();
        this.lng = post.getLng();
        this.address = post.getAddress();
        this.uploadImageUrl = post.getImageFileName();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PostUserInfo {

        private String nickname;
        private String username;
        private String profile;
    }
}
