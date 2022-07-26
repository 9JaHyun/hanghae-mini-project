package com.miniproject.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miniproject.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String content;

    private LocalDateTime createdAt;
    @JsonProperty(value = "writer")
    private CommentWriterDto commentWriterDto;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.commentWriterDto = new CommentWriterDto(
              comment.getUser().getUsername(),
              comment.getUser().getNickname(),
              comment.getUser().getProfile());
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

    @Data
    @AllArgsConstructor
    static class CommentWriterDto {
        private String username;
        private String nickname;
        private String profile;
    }

}










