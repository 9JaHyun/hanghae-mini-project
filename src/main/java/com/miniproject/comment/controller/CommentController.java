package com.miniproject.comment.controller;

import com.miniproject.comment.dto.CommentRequestDto;
import com.miniproject.comment.dto.CommentResponseDto;
import com.miniproject.comment.service.CommentService;
import com.miniproject.config.security.domain.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    //      댓글작성
    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<Void> saveComment(@PathVariable Long postId,
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody CommentRequestDto commentRequestDto) {
        String username = userDetails.getUsername();
        commentService.saveComment(username, postId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
              .body(null);
    }

    //댓글리스트
    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<CommentWrapper<List<CommentResponseDto>>> listComment(@PathVariable Long postId) {
        List<CommentResponseDto> dtoList = commentService.listComment(postId);
        return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_JSON)
              .body(new CommentWrapper<>(dtoList));
    }

    //댓글 삭제
    @DeleteMapping("posts/comment/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long commentId
          , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId,userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @Data
    @AllArgsConstructor
    static class CommentWrapper<T> {
        private T comments;
    }
}





