package com.miniproject.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponselistDto {

    private Long commentId;
    private String content;
    private String nickname;
    private Long modifiedAt;
}
