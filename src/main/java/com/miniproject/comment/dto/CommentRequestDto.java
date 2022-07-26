package com.miniproject.comment.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

@Setter
public class CommentRequestDto {
    private String content;
    public String getContent() {
        return content;
    }
}
