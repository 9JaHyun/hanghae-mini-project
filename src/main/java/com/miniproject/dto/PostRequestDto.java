package com.miniproject.dto;

import lombok.*;

import java.nio.file.Path;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostRequestDto {

    private String nickname;

    private String contents;

    private String imageFileName;

    private String lat;

    private String lng;

    private String address;

}
