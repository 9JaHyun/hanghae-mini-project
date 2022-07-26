package com.miniproject.post.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostRequestDto {
    private String contents;
    private String imageFileName;
    private String lat;
    private String lng;
    private String address;
}
