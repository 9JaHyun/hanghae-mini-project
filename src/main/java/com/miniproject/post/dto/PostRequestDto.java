package com.miniproject.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private String contents;
    private String lat;
    private String lng;
    private String address;
    private MultipartFile data;
}
