package com.miniproject.Comment.Controller;

import com.miniproject.Comment.Dto.CommentRequestDto;
import com.miniproject.Comment.Dto.CommentResponseDto;
import com.miniproject.Comment.Service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    //      댓글작성
    @PostMapping("posts/{postId}/comments")
    public CommentRequestDto saveComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        commentService.saveComment(postId, commentRequestDto);
        return commentRequestDto;
    }

    //댓글리스트
    @GetMapping("posts/{postId}/comments")
    public List<CommentResponseDto> listcomment(@PathVariable Long postId,@RequestBody CommentRequestDto commentRequestDto){
        return commentService.listcomment(postId);
    }

    //댓글 삭제
    @DeleteMapping("posts/comment/{commentId}")
    public Long deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return commentId;
    }





}





