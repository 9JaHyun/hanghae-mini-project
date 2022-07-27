package com.miniproject.post.dto;

import com.miniproject.post.model.Image;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ImageRequestDto {
    public String imageFileName;
    public String uploadImageUrl;
    public String originName;


    public ImageRequestDto(String uploadImageUrl, String imageFileName, String originName) {
        this.imageFileName = imageFileName;
        this.uploadImageUrl = uploadImageUrl;
        this.originName = originName;

    }
}
