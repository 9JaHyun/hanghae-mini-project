package com.miniproject.Comment.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miniproject.Comment.domain.Comment;
import lombok.*;

import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    @JsonProperty

//    private List<CommentResponselistDto> dtolist;
    private Long commentId;
    private String content;
    private Long postId;
//    private List<UserContentResponseDto> writer;
    private Long modifiedAt;
    private String username;
    private String nickname;


    public CommentResponseDto(Comment comment) {
        this.commentId=comment.getCommentId();
        this.content=comment.getContent();
        this.modifiedAt=comment.getModifiedAt();
        this.postId=comment.getPost().getId();
        this.username=comment.getUsername();
        this.nickname=comment.getNickname();

    }


//    public static CommentResponseDto EntitytoDto(Comment comment) {
//       return new CommentRequestDto(
//               comment.getContent()
//       );

    }










